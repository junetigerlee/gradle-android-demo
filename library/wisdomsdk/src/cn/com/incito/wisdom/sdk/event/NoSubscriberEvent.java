package cn.com.incito.wisdom.sdk.event;

/**
 * This Event is posted by EventBus when no subscriber is found for a posted event.
 *
 */
public final class NoSubscriberEvent {
    /** The {@link EventBus} instance to with the original event was posted to. */
    public final EventBus eventBus;

    /** The original event that could not be delivered to any subscriber. */
    public final Object originalEvent;

    public NoSubscriberEvent(EventBus eventBus, Object originalEvent) {
        this.eventBus = eventBus;
        this.originalEvent = originalEvent;
    }

}
