package net.myclient.events;

import net.minecraft.network.Packet;
import net.myclient.event.CancellableEvent;
import net.myclient.event.Listener;

import java.util.ArrayList;

public interface SentPacketListener extends Listener {
    void onSentPacket(SentPacketEvent event);

    class SentPacketEvent
            extends CancellableEvent<SentPacketListener>
    {
        private Packet<?> packet;

        public SentPacketEvent(Packet<?> packet)
        {
            this.packet = packet;
        }

        public Packet<?> getPacket()
        {
            return packet;
        }

        public void setPacket(Packet<?> packet)
        {
            this.packet = packet;
        }

        @Override
        public void fire(ArrayList<SentPacketListener> listeners)
        {
            for(SentPacketListener listener : listeners)
            {
                listener.onSentPacket(this);

                if(isCancelled())
                    break;
            }
        }

        @Override
        public Class<SentPacketListener> getListenerType()
        {
            return SentPacketListener.class;
        }
    }
}
