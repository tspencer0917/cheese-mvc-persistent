package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping()
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu menu, Errors errors, Model model){

        if(errors.hasErrors()){
            model.addAttribute(new Menu());
            model.addAttribute("title", "Add Menu");

            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:";
    }

    @RequestMapping(value = "/view/{id}")
    public String viewMenu(Model model, @PathVariable("id") int id) {

        Menu menu = menuDao.findOne(id);

        model.addAttribute("title", menu.getName());
        model.addAttribute("menu", menu);

        return "menu/view";
    }


    @RequestMapping(value = "/add-item/{id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable("id") int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("form", new AddMenuItemForm(menu, cheeseDao.findAll()));
        model.addAttribute("title", "Add item to menu: " + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value = "/add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm addMenuItemForm,
                      Errors errors,
                      Model model,
                      @RequestParam int menuId) {


        Menu menu = menuDao.findOne(menuId);

        if(errors.hasErrors()){
            model.addAttribute("form", new AddMenuItemForm(menu, cheeseDao.findAll()));
            model.addAttribute("title", "Add item to menu: " + menu.getName());

            return "redirect:/add-item" + menuId;
        }

        menu.addItem(cheeseDao.findOne(addMenuItemForm.getCheeseId()));
        menuDao.save(menu);
        return "redirect:";
    }

}
