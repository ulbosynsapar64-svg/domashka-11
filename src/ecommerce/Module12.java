package ecommerce;

import java.util.ArrayList;
import java.util.List;

class Candidate {
    String name;
    boolean approved;
    boolean offerAccepted;

    Candidate(String name) {
        this.name = name;
        this.approved = false;
        this.offerAccepted = false;
    }
}

class JobRequest {
    String title;
    boolean approved;

    JobRequest(String title) {
        this.title = title;
        this.approved = false;
    }
}

class HR {
    boolean reviewJobRequest(JobRequest request) {
        if(request.title.toLowerCase().contains("требования")) {
            request.approved = true;
        }
        return request.approved;
    }

    boolean reviewCandidate(Candidate candidate) {
        return candidate.name != null && !candidate.name.isEmpty();
    }

    boolean initialInterview(Candidate candidate) {
        return true;
    }

    String notifyIT(Candidate candidate) {
        return "IT notified for " + candidate.name;
    }
}

class Manager {
    JobRequest createRequest(String title) {
        return new JobRequest(title);
    }

    boolean technicalInterview(Candidate candidate) {
        return true;
    }
}

class SystemHire {
    String addEmployee(Candidate candidate) {
        return candidate.name + " added to database";
    }
}

// --- Процесс бронирования мероприятий ---

class Client {
    boolean requestAvailability(EventSystem system, String date) {
        return system.checkAvailability(date);
    }

    boolean confirmBooking(EventSystem system, String date, PaymentGateway payment) {
        boolean paid = payment.processPayment();
        if(paid) {
            system.confirm(date);
        }
        return paid;
    }
}

class EventSystem {
    List<String> bookedDates = new ArrayList<>();

    boolean checkAvailability(String date) {
        return !bookedDates.contains(date);
    }

    void confirm(String date) {
        bookedDates.add(date);
    }

    void notifyContractors(List<Contractor> contractors, String task) {
        for(Contractor c : contractors) {
            c.receiveTask(task);
        }
    }

    void sendReport(Admin admin) {
        admin.receiveReport("All tasks completed");
    }

    void collectFeedback(Client client, ManagerBooking manager) {
        manager.receiveFeedback("Feedback collected from client");
    }
}

class PaymentGateway {
    boolean processPayment() {
        return true;
    }
}

class Admin {
    void receiveReport(String report) {}
}

class Contractor {
    void receiveTask(String task) {}
}

class ManagerBooking {
    void receiveFeedback(String feedback) {}
}

// --- Main ---
public class Module12 {
    public static void main(String[] args) {

        // Процесс найма
        Manager manager = new Manager();
        HR hr = new HR();
        SystemHire systemHire = new SystemHire();

        JobRequest request = manager.createRequest("Новая вакансия: Требования соответствуют");
        if(hr.reviewJobRequest(request)) {
            List<Candidate> candidates = new ArrayList<>();
            candidates.add(new Candidate("Иван"));
            candidates.add(new Candidate("Мария"));

            for(Candidate c : candidates) {
                if(hr.reviewCandidate(c)) {
                    if(hr.initialInterview(c) && manager.technicalInterview(c)) {
                        c.approved = true;
                        c.offerAccepted = true;
                        systemHire.addEmployee(c);
                        hr.notifyIT(c);
                    }
                }
            }
        }

        // Процесс бронирования мероприятий
        EventSystem eventSystem = new EventSystem();
        Client client = new Client();
        PaymentGateway payment = new PaymentGateway();
        Admin admin = new Admin();
        ManagerBooking managerBooking = new ManagerBooking();
        List<Contractor> contractors = new ArrayList<>();
        contractors.add(new Contractor());
        contractors.add(new Contractor());

        String date = "2025-12-15";
        if(client.requestAvailability(eventSystem, date)) {
            if(client.confirmBooking(eventSystem, date, payment)) {
                eventSystem.notifyContractors(contractors, "Prepare venue tasks");
                eventSystem.sendReport(admin);
                eventSystem.collectFeedback(client, managerBooking);
            }
        }
    }
}
