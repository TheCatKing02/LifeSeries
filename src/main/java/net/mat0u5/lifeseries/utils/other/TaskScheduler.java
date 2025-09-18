package net.mat0u5.lifeseries.utils.other;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.mat0u5.lifeseries.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskScheduler {

    private static final List<Task> tasks = new ArrayList<>();
    private static final List<Task> newTasks = new ArrayList<>();

    public static void scheduleTask(int tickNumber, Runnable goal) {
        if (Main.MOD_DISABLED) return;
        Task task = new Task(tickNumber, goal);
        newTasks.add(task);
    }

    public static void registerTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (Main.MOD_DISABLED) {
                tasks.clear();
                newTasks.clear();
                return;
            }

            try {
                Iterator<Task> iterator = tasks.iterator();

                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    task.tickCount--;

                    if (task.tickCount <= 0) {
                        try {
                            task.goal.run();
                        }catch(Exception e) {
                            Main.LOGGER.error("Fatal error while running task " + task);
                            e.printStackTrace();
                        }
                        iterator.remove();
                    }
                }

                tasks.addAll(newTasks);
                newTasks.clear();
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    public static class Task {
        private int tickCount;
        private final Runnable goal;

        public Task(int tickCount, Runnable goal) {
            this.tickCount = tickCount;
            this.goal = goal;
        }
    }
}
