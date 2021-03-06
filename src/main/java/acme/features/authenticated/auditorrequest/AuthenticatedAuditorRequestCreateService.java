
package acme.features.authenticated.auditorrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditorrequest.Auditorrequest;
import acme.entities.configuration.Configuration;
import acme.entities.roles.Auditor;
import acme.features.utiles.ConfigurationRepository;
import acme.features.utiles.Spamfilter;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class AuthenticatedAuditorRequestCreateService implements AbstractCreateService<Authenticated, Auditorrequest> {

	// Internal state ---------------------------------------------------------------------------------------------------

	@Autowired
	private AuthenticatedAuditorRequestRepository	repository;
	@Autowired
	private ConfigurationRepository					configurationRepository;

	// AbstractCreateService<Authenticated, AuditorRequest> inteface ----------------------------------------------------


	@Override
	public boolean authorise(final Request<Auditorrequest> request) {
		assert request != null;
		Principal principal;

		principal = request.getPrincipal();

		return !principal.hasRole(Auditor.class);
	}

	@Override
	public void bind(final Request<Auditorrequest> request, final Auditorrequest entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);

	}

	@Override
	public void unbind(final Request<Auditorrequest> request, final Auditorrequest entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model, "description", "firm", "respStatement", "hasRequest", "auditorInDatabase");
	}

	@Override
	public Auditorrequest instantiate(final Request<Auditorrequest> request) {
		assert request != null;

		boolean isAuditor;
		Date now;
		Auditorrequest result;
		Auditor auditor;
		result = new Auditorrequest();
		result.setStatus("pending");

		now = new Date(System.currentTimeMillis() - 1);
		result.setMoment(now);
		Principal principal = request.getPrincipal();
		int id = principal.getAccountId();
		Authenticated user = this.repository.findOneUserAccountById(id);

		result.setUser(user);

		auditor = this.repository.findOneAuditorByUserId(principal.getAccountId());
		isAuditor = auditor == null;
		if (isAuditor) {
			result.setAuditorInDatabase(false);
		} else {
			result.setAuditorInDatabase(true);
		}

		return result;
	}

	@Override
	public void validate(final Request<Auditorrequest> request, final Auditorrequest entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		boolean hasDescription, isAuditor, hasSpamDescription, hasFirm, hasSpamFirm, hasRepStatement, hasSpamStatement, canRequest;
		Principal principal;
		Configuration configuration;
		String spamWords;
		Double spamThreshold;
		Authenticated user;
		Collection<Auditorrequest> requests;
		Integer id;
		Auditor auditor;

		principal = request.getPrincipal();
		id = principal.getAccountId();
		user = this.repository.findOneUserAccountById(id);

		auditor = this.repository.findOneAuditorByUserId(principal.getAccountId());
		isAuditor = auditor == null;
		errors.state(request, isAuditor, "firm", "authenticated.auditorrequest.error.you-are-auditor");

		if (isAuditor) {

			if (this.repository.allRequestByUserAccountId(user.getId()) == null) {
				requests = new ArrayList<Auditorrequest>();
			} else {
				requests = this.repository.allRequestByUserAccountId(user.getId());
			}

			canRequest = requests.isEmpty();
			errors.state(request, canRequest, "firm", "authenticated.auditorrequest.error.cannot-request");

			if (canRequest) {

				configuration = this.configurationRepository.findConfiguration();

				spamWords = configuration.getSpamWords();
				spamThreshold = configuration.getSpamThreshold();

				hasDescription = entity.getDescription() != null && !entity.getDescription().isEmpty();
				errors.state(request, hasDescription, "description", "authenticated.auditorrequest.error.must-have-description");

				if (hasDescription) {

					hasSpamDescription = Spamfilter.spamThreshold(entity.getDescription(), spamWords, spamThreshold);
					errors.state(request, !hasSpamDescription, "description", "authenticated.auditorrequest.error.must-not-have-spam-description");
				}

				hasFirm = entity.getFirm() != null && !entity.getFirm().isEmpty();
				errors.state(request, hasDescription, "firm", "authenticated.auditorrequest.error.must-have-firm");

				if (hasFirm) {

					hasSpamFirm = Spamfilter.spamThreshold(entity.getFirm(), spamWords, spamThreshold);
					errors.state(request, !hasSpamFirm, "firm", "authenticated.auditorrequest.error.must-not-have-spam-firm");
				}

				hasRepStatement = entity.getRespStatement() != null && !entity.getRespStatement().isEmpty();
				errors.state(request, hasRepStatement, "respStatement", "authenticated.auditorrequest.error.must-have-respStatement");

				if (hasRepStatement) {

					hasSpamStatement = Spamfilter.spamThreshold(entity.getRespStatement(), spamWords, spamThreshold);
					errors.state(request, !hasSpamStatement, "respStatement", "authenticated.auditorrequest.error.must-not-have-spam-respStatement");
				}
			}

			boolean ErrorPattern = entity.getStatus().matches("^(pending)|(accepted)|(rejected)$");
			errors.state(request, ErrorPattern, "status", "authenticated.auditorrequest.error.pattern-status");

		}
	}

	@Override
	public void create(final Request<Auditorrequest> request, final Auditorrequest entity) {
		assert request != null;
		assert entity != null;

		Date now;
		Principal principal;
		Integer id;
		Authenticated user;

		principal = request.getPrincipal();
		id = principal.getAccountId();
		now = new Date(System.currentTimeMillis() - 1);

		user = this.repository.findOneUserAccountById(id);

		user.setHasAuditorRequest(true);
		this.repository.save(user);

		entity.setUser(user);
		entity.setStatus("pending");
		entity.setMoment(now);

		this.repository.save(entity);

	}

}
