package net.myclient.events;

import net.minecraft.network.Packet;
import net.myclient.event.CancellableEvent;
import net.myclient.event.Listener;

import java.util.ArrayList;

public interface ReceivedPacketListener extends Listener {
    void onReceivedPacket(ReceivedPacketEvent event);

    class ReceivedPacketEvent
            extends CancellableEvent<ReceivedPacketListener>
    {
        private final Packet<?> packet;

        public ReceivedPacketEvent(Packet<?> packet)
        {
            this.packet = packet;
        }

        public Packet<?> getPacket()
        {
            return packet;
        }

        @Override
        public void fire(ArrayList<ReceivedPacketListener> listeners)
        {
            for(ReceivedPacketListener listener : listeners)
            {
                listener.onReceivedPacket(this);

                if(isCancelled())
                    break;
            }
        }

        @Override
        public Class<ReceivedPacketListener> getListenerType()
        {
            return ReceivedPacketListener.class;
        }
    }
}
