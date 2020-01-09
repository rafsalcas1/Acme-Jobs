
package acme.features.administrator.comercialbanner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import acme.entities.comercialbanner.Comercialbanner;
import acme.framework.components.BasicCommand;
import acme.framework.controllers.AbstractController;
import acme.framework.entities.Administrator;

@Controller
@RequestMapping("/administrator/comercialbanner/")
public class AdministratorComercialbannerController extends AbstractController<Administrator, Comercialbanner> {

	@Autowired
	private AdministratorComercialbannerListService		listService;

	@Autowired
	private AdministratorComercialbannerShowService		showService;

	@Autowired
	private AdministratorComercialbannerCreateService	createService;

	@Autowired
	private AdministratorComercialbannerUpdateService	updateService;

	@Autowired
	private AdministratorComercialbannerDeleteService	deleteService;


	@PostConstruct
	private void initialise() {
		super.addBasicCommand(BasicCommand.LIST, this.listService);
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
		super.addBasicCommand(BasicCommand.CREATE, this.createService);
		super.addBasicCommand(BasicCommand.UPDATE, this.updateService);
		super.addBasicCommand(BasicCommand.DELETE, this.deleteService);
	}
}
