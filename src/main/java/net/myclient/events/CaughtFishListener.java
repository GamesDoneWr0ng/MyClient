package net.myclient.events;

import net.myclient.event.Event;
import net.myclient.event.Listener;

import java.util.ArrayList;

public interface CaughtFishListener extends Listener {
    void onCaughtFish(Boolean caughtFish);

    class CaughtFishEvent extends Event<CaughtFishListener> {
        private final Boolean caughtFish;

        public CaughtFishEvent(Boolean caughtFish)
        {
            this.caughtFish = caughtFish;
        }

        @Override
        public void fire(ArrayList<CaughtFishListener> listeners) {
            for(CaughtFishListener listener : listeners)
                listener.onCaughtFish(caughtFish);
        }

        @Override
        public Class<CaughtFishListener> getListenerType() {
            return CaughtFishListener.class;
        }
    }
}
