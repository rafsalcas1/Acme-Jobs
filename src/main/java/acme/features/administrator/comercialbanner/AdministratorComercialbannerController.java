
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
	private AdministratorComercialbannerListService	listService;

	@Autowired
	private AdministratorComercialbannerShowService	showService;


	@PostConstruct
	private void initialise() {
		super.addBasicCommand(BasicCommand.LIST, this.listService);
		super.addBasicCommand(BasicCommand.SHOW, this.showService);
	}
}
