package top.bearcabbage.lanterninstorm.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import top.bearcabbage.lanterninstorm.entity.SpiritLanternEntity;

@Environment(EnvType.CLIENT)
public class PrivateLanternRenderer extends SpiritLanternEntityRenderer {
    private final static float SIZE = 1.0f;

    public PrivateLanternRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(SpiritLanternEntity Entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(SIZE,SIZE,SIZE);
        super.render(Entity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
