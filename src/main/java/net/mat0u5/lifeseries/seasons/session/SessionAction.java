package net.mat0u5.lifeseries.seasons.session;

import static net.mat0u5.lifeseries.Main.currentSession;

public abstract class SessionAction {
    public boolean hasTriggered = false;
    public int triggerAtTicks;
    public String sessionMessage;
    public String sessionId;

    public SessionAction(int triggerAtTicks) {
        this.triggerAtTicks = triggerAtTicks;
    }

    public SessionAction(int triggerAtTicks, String message, String sessionId) {
        this.triggerAtTicks = triggerAtTicks;
        this.sessionMessage = message;
        this.sessionId = sessionId;
    }

    public boolean tick() {
        boolean shouldTrigger = shouldTrigger();
        if (hasTriggered && !shouldTrigger) hasTriggered = false;
        if (hasTriggered) return true;
        if (shouldTrigger) {
            hasTriggered = true;
            SessionTranscript.triggerSessionAction(sessionId);
            trigger();
            return true;
        }
        return false;
    }

    public boolean shouldTrigger() {
        if (triggerAtTicks >= 0) {
            // Trigger after start
            int passedTime = currentSession.getPassedTime();
            return passedTime >= triggerAtTicks;
        }
        else {
            // Trigger before end
            int remainingTime = currentSession.getRemainingTime();
            return remainingTime <= Math.abs(triggerAtTicks);
        }
    }

    public int getTriggerTime() {
        return triggerAtTicks;
    }

    public abstract void trigger();
}
