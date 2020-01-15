
package acme.features.sponsor.comercialbanner;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.comercialbanner.Comercialbanner;
import acme.entities.configuration.Configuration;
import acme.entities.roles.Sponsor;
import acme.features.utiles.ConfigurationRepository;
import acme.features.utiles.Spamfilter;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class SponsorComercialbannerCreateService implements AbstractCreateService<Sponsor, Comercialbanner> {

	// Internal state ------------------------------------------------------------------------------------------------

	@Autowired
	private SponsorComercialbannerRepository	repository;

	@Autowired
	private ConfigurationRepository				ConfigurationRepository;


	// AbstractCreateService<Sponsor, Comercialbanner> interface ------------------------------------------------------

	@Override
	public boolean authorise(final Request<Comercialbanner> request) {
		assert request != null;
		return true;
	}

	@Override
	public void bind(final Request<Comercialbanner> request, final Comercialbanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "expiration");

	}

	@Override
	public void unbind(final Request<Comercialbanner> request, final Comercialbanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "urlPicture", "slogan", "urlTarget", "finalMode", "creditNumber", "name", "surname", "securityCode", "type");

	}

	@Override
	public Comercialbanner instantiate(final Request<Comercialbanner> request) {
		assert request != null;

		Sponsor sponsor;
		Principal principal;
		Comercialbanner result;
		int principalId;
		Date expiration = new Date();

		principal = request.getPrincipal();
		principalId = principal.getAccountId();

		sponsor = this.repository.findOneSponsorByUserAccountId(principalId);

		result = new Comercialbanner();
		result.setFinalMode(false);
		result.setSponsor(sponsor);
		result.setExpiration(expiration);
		return result;
	}

	@Override
	public void validate(final Request<Comercialbanner> request, final Comercialbanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Configuration configuration;
		String spamWords;
		Double spamThreshold;

		boolean hasSlogan, hasSpamSlogan, hasExpiration;
		boolean isFuture, hasNumber, hasNameOwner, hasSurname, hasSecurityCode;
		Date now;
		now = new Date(System.currentTimeMillis() - 1);

		// Slogan validation ---------------------------------------------------------------------------------

		if (!errors.hasErrors("slogan")) {
			hasSlogan = entity.getSlogan() != null && !entity.getSlogan().isEmpty();
			errors.state(request, hasSlogan, "slogan", "sponsor.comercialbanner.error.must-have-slogan");

			if (hasSlogan) {

				configuration = this.ConfigurationRepository.findConfiguration();
				spamWords = configuration.getSpamWords();
				spamThreshold = configuration.getSpamThreshold();

				hasSpamSlogan = Spamfilter.spamThreshold(entity.getSlogan(), spamWords, spamThreshold);
				errors.state(request, !hasSpamSlogan, "slogan", "sponsor.comercialbanner.error.must-have-not-spam-slogan");
			}
		}

		// Expiration validation ------------------------------------------------------------------------------------

		if (!errors.hasErrors("creditExp")) {
			hasExpiration = request.getModel().getString("creditExp") != null;
			errors.state(request, hasExpiration, "creditExp", "sponsor.comercialbanner.error.must-have-expiration");
			if (hasExpiration) {
				String creditExp = request.getModel().getString("creditExp");
				String start = creditExp.substring(0, 2);
				boolean start00 = start.equals("00");
				errors.state(request, !start00, "creditExp", "sponsor.comercialbanner.error.month-00");
				if (!start00) {
					boolean valid = creditExp.matches("^[0-9]{2}[/][0-9]{2}$");
					errors.state(request, valid, "creditExp", "sponsor.comercialbanner.error.expiration-pattern");
					if (valid) {
						Calendar date = Calendar.getInstance();
						String fecha = request.getModel().getString("creditExp");
						String[] sa = fecha.split("/");
						int year = Integer.valueOf("20" + sa[1].trim());
						int month = Integer.valueOf(sa[0].trim());
						date.set(Calendar.MONTH, month);
						date.set(Calendar.YEAR, year);
						date.set(Calendar.DAY_OF_MONTH, 1);
						isFuture = now.before(date.getTime());
						errors.state(request, isFuture, "creditExp", "sponsor.comercialbanner.error.expirated");
					}
				}
			}
		}

		// Number validation ----------------------------------------------------------------------------------------

		if (!errors.hasErrors("creditNumber")) {
			hasNumber = entity.getCreditNumber() != null;
			errors.state(request, hasNumber, "creditNumber", "sponsor.comercialbanner.error.must-have-creditNumber");
		}
		// Name validation ------------------------------------------------------------------------------------------

		if (!errors.hasErrors("name")) {
			hasNameOwner = entity.getName() != null;
			errors.state(request, hasNameOwner, "name", "sponsor.comercialbanner.error.must-have-name");
		}
		// Surname validation ---------------------------------------------------------------------------------------

		if (!errors.hasErrors("surname")) {
			hasSurname = entity.getSurname() != null;
			errors.state(request, hasSurname, "surname", "sponsor.comercialbanner.error.must-have-surname");
		}
		// Security code validation ----------------------------------------------------------------------------------

		if (!errors.hasErrors("securityCode")) {
			hasSecurityCode = entity.getSecurityCode() != null;
			errors.state(request, hasSecurityCode, "securityCode", "sponsor.comercialbanner.error.must-have-securityCode");
		}
		boolean hasSecurityCodeP = entity.getSecurityCode().matches("^[0-9]{3}$");
		errors.state(request, hasSecurityCodeP, "securityCode", "sponsor.comercialbanner.error.pattern-securityCode");
		boolean ErrorPatterntype = entity.getType().matches("^(Dinners Club)|(Visa)|(Master Card)|(American Express)$");
		errors.state(request, ErrorPatterntype, "type", "sponsor.comercialbanner.error.pattern-type");

	}

	@Override
	public void create(final Request<Comercialbanner> request, final Comercialbanner entity) {
		assert request != null;
		assert entity != null;

		boolean hasExpiration = request.getModel().getString("creditExp") != null;
		if (hasExpiration) {
			Calendar date = Calendar.getInstance();
			String fecha = request.getModel().getString("creditExp");
			String[] sa = fecha.split("/");
			date.set(Calendar.MONTH, Integer.valueOf(sa[0].trim()));
			date.set(Calendar.YEAR, Integer.valueOf("20" + sa[1].trim()));
			date.set(Calendar.DAY_OF_MONTH, 1);
			entity.setExpiration(date.getTime());
		}

		this.repository.save(entity);

	}

}
