package org.kubithon.replicate.replication.protocol;

/**
 * The packet holding the look data of a sponsor. The pitch and the yaw are stored in one byte for each, using the
 * format specified on wiki.vg. In a nutshell, the rotations are stored as 1/255 of a complete turn. That is to say
 * that if the byte is equal to 0, the rotation is equal to 0°, and if the byte is equal to 255, the rotation is
 * equal to 360°.
 *
 * @author troopy28
 * @since 1.0.0
 */
public class PlayerLookKubicket extends KubithonPacket {
    private float pitch;
    private float yaw;
    private byte pitchByte;
    private byte yawByte;

    PlayerLookKubicket() {
        super(KubicketType.PLAYER_LOOK);
    }

    @Override
    protected void composePacket() {
        writeByte(KubithonPacket.getByteFromAngle(pitch));
        writeByte(KubithonPacket.getByteFromAngle(yaw));
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
