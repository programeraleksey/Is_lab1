package com.example.web;

import com.example.service.event.WorkersChanged;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;

@ApplicationScoped
public class WorkerPushNotifier {

    @Inject
    @Push
    private PushContext workers;

    public void onWorkersChanged(@Observes(during = TransactionPhase.AFTER_SUCCESS) WorkersChanged event) {
        workers.send("updated");
    }
}