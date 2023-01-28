package net.myclient.events;

import net.myclient.event.Event;
import net.myclient.event.Listener;
import net.myclient.mixinterface.IClientPlayerEntity;

import java.util.ArrayList;

public interface PlayerMoveListener extends Listener
{
    void onPlayerMove(IClientPlayerEntity player);

    class PlayerMoveEvent extends Event<PlayerMoveListener>
    {
        private final IClientPlayerEntity player;

        public PlayerMoveEvent(IClientPlayerEntity player) {
            this.player = player;
        }

        @Override
        public void fire(ArrayList<PlayerMoveListener> listeners) {
            for(PlayerMoveListener listener : listeners)
                listener.onPlayerMove(player);
        }

        @Override
        public Class<PlayerMoveListener> getListenerType() {
            return PlayerMoveListener.class;
        }
    }
}