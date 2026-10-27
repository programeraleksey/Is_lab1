package com.example.web;

import com.example.dto.WorkerFilter;
import com.example.entity.Position;
import com.example.entity.Worker;
import com.example.service.WorkerService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WorkerListBean implements Serializable {
    private static final int PAGE_SIZE = 10;

    @Inject
    private WorkerService workerService;

    private WorkerFilter filter;
    private List<Worker> workers;
    private long total;
    private int page;

    @PostConstruct
    private void init() {
        filter = new WorkerFilter();
        page = 0;
        load();
    }

    private void load() {
        workers = workerService.findPage(filter, page, PAGE_SIZE);
        total = workerService.count(filter);
    }

    public void refresh() {
        load();
        if (page >= getTotalPages()) {
            page = getTotalPages() - 1;
            load();
        }
    }

    public void next() {
        page++;
        load();
    }

    public void prev() {
        page--;
        load();
    }

    public boolean isHasNext() {
        return total > (long) PAGE_SIZE * (page + 1);
    }

    public boolean isHasPrev() {
        return page > 0;
    }

    public int getTotalPages() {
        if (total == 0) {
            return 1;
        }
        return (int) Math.ceil((double) total / PAGE_SIZE);
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public WorkerFilter getFilter() {
        return filter;
    }

    public Position[] getPositions() {
        return Position.values();
    }

    public void search() {
        page = 0;
        load();
    }

    public void reset() {
        filter = new WorkerFilter();
        page = 0;
        load();
    }

    public void sortBy(String field) {
        if (field.equals(filter.getSortField())) {
            filter.setAscending(!filter.isAscending());
        }
        else {
            filter.setSortField(field);
            filter.setAscending(true);
        }
        page = 0;
        load();
    }

    public String sortIndicator(String field) {
        if (!field.equals(filter.getSortField())) {return "";}
        return filter.isAscending() ? "▲" : "▼";
    }
}
