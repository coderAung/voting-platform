package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.event.PollEndEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class PollSchedulingService {

    private final TaskScheduler scheduler;
    private final ApplicationEventPublisher publisher;
    private final Map<UUID, ScheduledFuture<?>> tasks = new HashMap<>();

    public void schedule(PollDetail poll) {
        Runnable task = () -> publisher.publishEvent(new PollEndEvent(poll));
        var future = scheduler.schedule(task, poll.endTime().atZone(ZoneId.systemDefault()).toInstant());
        tasks.put(poll.id(), future);
    }

    public void remove(UUID id) {
        var task = tasks.remove(id);
        if(task != null) {
            task.cancel(false);
        }
    }
}
