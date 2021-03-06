
package acme.features.administrator.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.application.Application;
import acme.entities.dashboard.Dashboard;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorDashboardShowService implements AbstractShowService<Administrator, Dashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorDashboardRepository repository;


	// AbstractShowService<Administrator, Dashboard> interface --------------

	@Override
	public boolean authorise(final Request<Dashboard> request) {
		assert request != null;
		return true;
	}

	@Override
	public void unbind(final Request<Dashboard> request, final Dashboard entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "totalAnnouncement", "totalInvestorsRecord", "totalCompanyRecords", "companysBySector", "sectorsOfCompanys", "inverstorsBySector", "sectorsOfInverstors", "jobsByFinalMode", "statusOfApplication", "applicationByStatus",
			"avgApplicationEmployer", "avgJobEmployer", "avgApplicationWorker", "diasPending", "applicationPendingPerDay", "applicationAcceptedPerDay", "applicationRejectedPerDay");

	}

	@Override
	public Dashboard findOne(final Request<Dashboard> request) {
		Dashboard result = new Dashboard();
		result.setTotalAnnouncement(this.getTotalAnnouncement());
		result.setTotalCompanyRecords(this.getTotalCompanyRecords());
		result.setTotalInvestorsRecord(this.getTotalInvestorsRecord());
		result.setCompanysBySector(this.getCompanysBySector());
		result.setInverstorsBySector(this.getInverstorsBySector());
		result.setSectorsOfCompanys(this.getSectorsOfCompanys());
		result.setSectorsOfInverstors(this.getSectorOfInverstors());
		result.setJobsByFinalMode(this.getJobsByFinalMode());
		result.setApplicationByStatus(this.getApplicationByStatus());
		result.setStatusOfApplication(this.getStatusOfApplication());
		result.setAvgJobEmployer(this.getMediaJobsPerEmployer());
		result.setAvgApplicationEmployer(this.getMediaApplicationPerEmployer());
		result.setAvgApplicationWorker(this.getMediaApplicationPerWorker());
		result.setDiasPending(this.getDiasPending(this.getPendingPerDay()));
		result.setApplicationPendingPerDay(this.getApplicationPendingPerDay(this.getPendingPerDay()));
		result.setApplicationAcceptedPerDay(this.getApplicationAcceptedPerDay(this.getAcceptedPerDay()));
		result.setApplicationRejectedPerDay(this.getApplicationRejectedPerDay(this.getRejectedPerDay()));

		return result;
	}

	public Integer getTotalAnnouncement() {
		Integer res = this.repository.countAnnouncement();
		return res;
	}

	public Integer getTotalInvestorsRecord() {
		Integer res = this.repository.countInvestorsRecords();
		return res;
	}

	public Integer getTotalCompanyRecords() {
		Integer res = this.repository.countCompanyRecord();
		return res;
	}

	public List<Integer> getCompanysBySector() {
		List<Integer> res = this.repository.getCompanysBySector();
		return res;
	}

	public List<String> getSectorsOfCompanys() {
		List<String> res = this.repository.getSectorsOfCompanys();
		return res;
	}

	public List<Integer> getInverstorsBySector() {
		List<Integer> res = this.repository.getInverstorsBySector();
		return res;
	}

	public List<String> getSectorOfInverstors() {
		List<String> res = this.repository.getSectorOfInverstors();
		return res;
	}

	public List<Double> getJobsByFinalMode() {
		List<Integer> result = this.repository.getJobsByFinalMode();
		Double size = result.stream().mapToDouble(x -> x).sum();
		List<Double> res = result.stream().map(x -> x.doubleValue() / size).collect(Collectors.toList());
		return res;
	}

	public List<String> getStatusOfApplication() {
		List<String> res = this.repository.getStatusOfApplication();
		return res;
	}

	public List<Double> getApplicationByStatus() {
		List<Integer> result = this.repository.getApplicationBystatus();
		Double size = result.stream().mapToDouble(x -> x).sum();
		List<Double> res = result.stream().map(x -> x.longValue() / size).collect(Collectors.toList());
		return res;

	}

	public Double getMediaJobsPerEmployer() {
		Double res = this.repository.getMediaJobsPerEmployer();
		return res;
	}

	public Double getMediaApplicationPerEmployer() {
		Double res = this.repository.getMediaApplicationPerEmployer();
		return res;
	}

	public Double getMediaApplicationPerWorker() {
		Double res = this.repository.getMediaApplicationPerWorker();
		return res;
	}

	// Pending application per day ----------------------------------------------------------------------------
	public Map<Integer, Integer> getPendingPerDay() {
		Map<Integer, Integer> res = new TreeMap<>();
		List<Application> result = this.repository.getPendingApplications();
		Date now = new Date(System.currentTimeMillis() - 1);
		Date dateApplication;
		Integer diasDiferencia;
		Integer suma;

		for (int i = -28; i <= 0; i++) {
			res.put(i, 0);
		}

		for (Application a : result) {

			dateApplication = a.getMoment();

			diasDiferencia = (int) ((dateApplication.getTime() - now.getTime()) / 86400000);

			if (-28 < diasDiferencia && diasDiferencia < 1) {

				suma = res.get(diasDiferencia) + 1;
				res.put(diasDiferencia, suma);

			}

		}

		return res;
	}

	public List<Integer> getDiasPending(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> res = new ArrayList<>(applicationsPerDay.keySet());
		return res;

	}

	public List<Integer> getApplicationPendingPerDay(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> dias = new ArrayList<>(applicationsPerDay.keySet());
		List<Integer> res = new ArrayList<>();

		for (Integer d : dias) {

			res.add(applicationsPerDay.get(d));
		}

		return res;

	}

	// Accepted application per day ----------------------------------------------------------------------------
	public Map<Integer, Integer> getAcceptedPerDay() {
		Map<Integer, Integer> res = new TreeMap<>();
		List<Application> result = this.repository.getAcceptedApplications();
		Date now = new Date(System.currentTimeMillis() - 1);
		Date dateApplication;
		Integer diasDiferencia;
		Integer suma;

		for (int i = -28; i <= 0; i++) {
			res.put(i, 0);
		}

		for (Application a : result) {

			dateApplication = a.getMoment();

			diasDiferencia = (int) ((dateApplication.getTime() - now.getTime()) / 86400000);

			if (-28 < diasDiferencia && diasDiferencia < 1) {

				suma = res.get(diasDiferencia) + 1;
				res.put(diasDiferencia, suma);

			}

		}

		return res;
	}

	public List<Integer> getDiasAccepted(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> res = new ArrayList<>(applicationsPerDay.keySet());
		return res;

	}

	public List<Integer> getApplicationAcceptedPerDay(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> dias = new ArrayList<>(applicationsPerDay.keySet());
		List<Integer> res = new ArrayList<>();

		for (Integer d : dias) {

			res.add(applicationsPerDay.get(d));
		}

		return res;

	}

	// Accepted application per day ----------------------------------------------------------------------------
	public Map<Integer, Integer> getRejectedPerDay() {
		Map<Integer, Integer> res = new TreeMap<>();
		List<Application> result = this.repository.getRejectedApplications();
		Date now = new Date(System.currentTimeMillis() - 1);
		Date dateApplication;
		Integer diasDiferencia;
		Integer suma;

		for (int i = -28; i <= 0; i++) {
			res.put(i, 0);
		}

		for (Application a : result) {

			dateApplication = a.getMoment();

			diasDiferencia = (int) ((dateApplication.getTime() - now.getTime()) / 86400000);

			if (-28 < diasDiferencia && diasDiferencia < 1) {

				suma = res.get(diasDiferencia) + 1;
				res.put(diasDiferencia, suma);

			}

		}

		return res;
	}

	public List<Integer> getDiasRejected(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> res = new ArrayList<>(applicationsPerDay.keySet());
		return res;

	}

	public List<Integer> getApplicationRejectedPerDay(final Map<Integer, Integer> applicationsPerDay) {

		List<Integer> dias = new ArrayList<>(applicationsPerDay.keySet());
		List<Integer> res = new ArrayList<>();

		for (Integer d : dias) {

			res.add(applicationsPerDay.get(d));
		}

		return res;

	}

}
