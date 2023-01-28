package net.myclient.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.myclient.event.EventManager;
import net.myclient.events.ReceivedPacketListener.ReceivedPacketEvent;
import net.myclient.events.SentPacketListener.SentPacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    private void logSentPacket(Packet<?> packet, PacketCallbacks callback, CallbackInfo ci) {
        SentPacketEvent event = new SentPacketEvent(packet);
        EventManager.fire(event);

        if(event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static void logReceivedPacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        ReceivedPacketEvent event = new ReceivedPacketEvent(packet);
        EventManager.fire(event);

        if(event.isCancelled())
            ci.cancel();
    }
}