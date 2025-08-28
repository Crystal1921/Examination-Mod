package com.mcg.examinationmod.network;
import com.mcg.examinationmod.ExaminationMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
@EventBusSubscriber(modid = ExaminationMod.MODID,bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ExaminationMod.MODID);
        registrar.playToServer(
                PlatformRequestPacket.TYPE,
                PlatformRequestPacket.STREAM_CODEC,
                PlatformRequestPacket::handle
                );

        registrar.playToClient(
                TeleportResponsePayload.TYPE,
                TeleportResponsePayload.STREAM_CODEC,
                TeleportResponsePayload::handle
                );
            }
        }

