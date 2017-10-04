package org.kubithon.replicate.util;

import org.kubithon.replicate.ReplicatePluginSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

/**
 * @author troopy28
 * @since 1.0.0
 */
public class SpongeScheduler {

    private SpongeScheduler() {
    }

    public static Task scheduleTask(Runnable runnable) {
        return Sponge.getScheduler().createTaskBuilder().execute(runnable).submit(ReplicatePluginSponge.get());
    }

    public static Task scheduleRepeatingTask(Runnable runnable, long delayTicks, long intervalTicks) {
        return Sponge.getScheduler().createTaskBuilder()
                .execute(runnable)
                .delayTicks(delayTicks)
                .intervalTicks(intervalTicks)
                .submit(ReplicatePluginSponge.get());
    }

    public static Task scheduleTaskLater(Runnable runnable, long delayTicks) {
        return Sponge.getScheduler().createTaskBuilder()
                .execute(runnable)
                .delayTicks(delayTicks)
                .submit(ReplicatePluginSponge.get());
    }

    public static Task scheduleTaskLaterAsync(Runnable runnable, long delayTicks) {
        return Sponge.getScheduler().createTaskBuilder()
                .execute(runnable)
                .delayTicks(delayTicks)
                .async()
                .submit(ReplicatePluginSponge.get());
    }

    public static Task scheduleRepeatingTaskAsync(Runnable runnable, long delayTicks, long intervalTicks) {
        return Sponge.getScheduler().createTaskBuilder()
                .execute(runnable)
                .delayTicks(delayTicks)
                .intervalTicks(intervalTicks)
                .async()
                .submit(ReplicatePluginSponge.get());
    }
}
