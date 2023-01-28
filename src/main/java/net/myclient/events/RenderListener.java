package net.myclient.events;

import net.minecraft.client.util.math.MatrixStack;
import net.myclient.event.Event;
import net.myclient.event.Listener;

import java.util.ArrayList;

public interface RenderListener extends Listener
{
    void onRender(MatrixStack matrixStack, float partialTicks);

    class RenderEvent extends Event<RenderListener>
    {
        private final MatrixStack matrixStack;
        private final float partialTicks;

        public RenderEvent(MatrixStack matrixStack, float partialTicks)
        {
            this.matrixStack = matrixStack;
            this.partialTicks = partialTicks;
        }

        @Override
        public void fire(ArrayList<RenderListener> listeners)
        {
            for(RenderListener listener : listeners)
                listener.onRender(matrixStack, partialTicks);
        }

        @Override
        public Class<RenderListener> getListenerType()
        {
            return RenderListener.class;
        }
    }
}