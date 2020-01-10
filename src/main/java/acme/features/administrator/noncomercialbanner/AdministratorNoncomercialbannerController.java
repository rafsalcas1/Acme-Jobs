
package acme.features.administrator.noncomercialbanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.noncomercialbanner.Noncomercialbanner;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Administrator;

@Controller
@RequestMapping("/administrator/noncomercialbanner/")
public class AdministratorNoncomercialbannerController extends AbstractController<Administrator, Noncomercialbanner> {

	@Autowired
	private AdministratorNoncomercialbannerListService	listService;

	@Autowired
	private AdministratorNoncomercialbannerShowService	showService;


	@PostConstruct
	private void initialise() {
		super.addBasicCommand(BasicCommand.LIST, this.listService);
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
	}
}
