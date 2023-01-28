package net.myclient.hack;

import net.myclient.hacks.*;
import net.myclient.packetShit.PacketLogger;

public final class HackManager {
    public final Fly fly = new Fly();
    public final AutoFish autoFish = new AutoFish();
    public final NoFall noFall = new NoFall();
    public final AnitVelocity anitVelocity = new AnitVelocity();
    public final FastBreak fastBreak = new FastBreak();
    public final PacketLogger packetLogger = new PacketLogger();
    public final AntiHunger antiHunger = new AntiHunger();
    public final AntiParticle antiParticle = new AntiParticle();
    public final FreeCam freeCam = new FreeCam();
}