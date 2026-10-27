package com.example.web;

import com.example.entity.Worker;
import com.example.exception.BusinessException;
import com.example.service.WorkerService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class WorkerViewBean implements Serializable {
    @Inject
    private WorkerService workerService;

    private Integer id;
    private Worker worker;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Worker getWorker() {
        return worker;
    }

    public void load() {
        try {
            worker = workerService.findById(id);
        } catch (BusinessException e) {
            error(e.getMessage());
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
        }
    }

    public void refresh() {
        if (worker == null) {
            return;
        }
        try {
            worker = workerService.findById(id);
        } catch (BusinessException e) {
            worker = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Работник с id " + id + " был удалён", null));
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
        }
    }

    public String delete() {
        try {
            workerService.delete(id);
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage("Работник удалён"));
            ctx.getExternalContext().getFlash().setKeepMessages(true);
            return "index?faces-redirect=true";
        } catch (BusinessException e) {
            error(e.getMessage());
            return null;
        } catch (RuntimeException e) {
            error(ErrorMessages.describe(e));
            return null;
        }
    }

    private void error(String text) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
    }
}
