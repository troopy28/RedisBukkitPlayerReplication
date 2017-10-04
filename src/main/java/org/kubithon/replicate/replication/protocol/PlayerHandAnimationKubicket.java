package org.kubithon.replicate.replication.protocol;


import net.minecraft.util.EnumHand;

/**
 * The packet used to notify a hand movement of a sponsor.
 *
 * @author troopy28
 * @since 1.0.0
 */
public class PlayerHandAnimationKubicket extends KubithonPacket {

    private byte hand;

    PlayerHandAnimationKubicket() {
        super(KubicketType.PLAYER_HAND_ANIMATION);

    }

    @Override
    protected void composePacket() {
        writeByte(hand);
    }

    void setHand(EnumHand hand) {
        this.hand = hand == EnumHand.MAIN_HAND ? 0 : (byte) 1;
    }

    void setHand(byte hand) {
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
    }
}
