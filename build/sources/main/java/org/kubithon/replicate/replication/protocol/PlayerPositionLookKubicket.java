package org.kubithon.replicate.replication.protocol;

/**
 * A combination of the {@link PlayerLookKubicket} and the {@link PlayerPositionKubicket}.
 *
 * @author troopy28
 * @since 1.0.0
 */

public class PlayerPositionLookKubicket extends KubithonPacket {
    private float xPos;
    private float yPos;
    private float zPos;
    private boolean onGround;

    private float pitch;
    private float yaw;
    private byte pitchByte;
    private byte yawByte;

    PlayerPositionLookKubicket() {
        super(KubicketType.PLAYER_POSITION_LOOK);
    }

    @Override
    protected void composePacket() {
        writeFloat(xPos);
        writeFloat(yPos);
        writeFloat(zPos);
        writeByte(KubithonPacket.getByteFromAngle(pitch));
        writeByte(KubithonPacket.getByteFromAngle(yaw));
        writeByte(onGround ? (byte) 1 : (byte) 0);
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getzPos() {
        return zPos;
    }

    public void setzPos(float zPos) {
        this.zPos = zPos;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public byte getPitchByte() {
        return pitchByte;
    }

    public void setPitchByte(byte pitchByte) {
        this.pitchByte = pitchByte;
    }

    public byte getYawByte() {
        return yawByte;
    }

    public void setYawByte(byte yawByte) {
        this.yawByte = yawByte;
    }
}