package net.mat0u5.lifeseries.mixin;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = PlayerListS2CPacket.class, priority = 1)
public interface PlayerListS2CPacketAccessor {
    @Mutable
    @Accessor
    void setEntries(List<PlayerListS2CPacket.Entry> entries);
}
