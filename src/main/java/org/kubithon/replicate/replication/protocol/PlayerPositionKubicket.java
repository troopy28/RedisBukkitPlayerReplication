package org.kubithon.replicate.replication.protocol;

/**
 * The packet containing the information relative to the position of a sponsor, that is to say the x, y, z and onGround
 * attributes.
 *
 * @author troopy28
 * @since 1.0.0
 */
public class PlayerPositionKubicket extends KubithonPacket {
    private float xPos;
    private float yPos;
    private float zPos;
    private boolean onGround;

    PlayerPositionKubicket() {
        super(KubicketType.PLAYER_POSITION);
    }

    @Override
    protected void composePacket() {
        writeFloat(xPos);
        writeFloat(yPos);
        writeFloat(zPos);
        writeByte(onGround ? (byte) 1 : (byte) 0);
    }

    public float getxPos() {
        return xPos;
    }

    void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getzPos() {
        return zPos;
    }

    void setzPos(float zPos) {
        this.zPos = zPos;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}