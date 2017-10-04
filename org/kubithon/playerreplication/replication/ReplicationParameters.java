package org.kubithon.playerreplication.replication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.kubithon.playerreplication.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by troopy28 on 20/02/2017.
 */
public class ReplicationParameters {

    private static final String PARAMETERS_FILE_LOCATION = "ReplicationParameters.json";

    private List<ReplicatedPlayer> replicablePlayers;
    private Gson gson;

    public ReplicationParameters() {
        replicablePlayers = new ArrayList<>();
        gson = new Gson();
        checkParamsFileExistence();
        fillListFromJson();
    }

    /**
     * Checks if the file containing the replication information exists. If not, create it.
     */
    private void checkParamsFileExistence() {
        if (!Utils.fileExists(PARAMETERS_FILE_LOCATION))
            saveParametersFile();
        else
            Log.info("Replication parameters exist.");
    }

    /**
     * Generates a file that contains the basic information about who to replicate.
     */
    public void saveParametersFile() {
        Log.info("Replication parameters does not exist. Creating them...");
        String jsonList = gson.toJson(replicablePlayers);
        Utils.writeFileContent(PARAMETERS_FILE_LOCATION, jsonList);
        Log.info("Done !");
    }

    private void fillListFromJson() {
        Log.info("Reading replication parameters.");
        Type listType = new TypeToken<List<ReplicatedPlayer>>() {
        }.getType();
        replicablePlayers = gson.fromJson(Utils.readFileContent(PARAMETERS_FILE_LOCATION), listType);
        Log.info("Done !");
        Log.info("We now replicate :");
        replicablePlayers.forEach(replicatedPlayer -> Log.info(replicatedPlayer.getReplicationId() + " <-> " + replicatedPlayer.getUuid()));
    }

    public ReplicatedPlayer addReplicablePlayer(UUID uuid) {
        ReplicatedPlayer replicatedPlayer = new ReplicatedPlayer(uuid, replicablePlayers.size());
        replicablePlayers.add(replicatedPlayer);
        return replicatedPlayer;
    }

    public void removeReplicablePlayer(UUID uuid) {
        replicablePlayers.stream().filter(rp -> rp.getUuid() == uuid).forEach(replicablePlayers::remove);
    }

    /**
     * @param uuid The UUID of the player you want to check.
     * @return Returns that a player is replicable according to the JSON parameters file. It means that the player must
     * be replicated WHEN he is connected, but maybe not currently.
     */
    public boolean isReplicable(UUID uuid) {
        for (ReplicatedPlayer rp : replicablePlayers) {
            if (rp.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Returns the list of players that are replicable according to the JSON parameters file. It means that these players must
     * be replicated WHEN they are connected, but maybe not currently.
     */
    public List<ReplicatedPlayer> getReplicablePlayers() {
        return replicablePlayers;
    }

    public ReplicatedPlayer getReplicatedPlayer(Player player) {
        for (ReplicatedPlayer rp : replicablePlayers) {
            if (rp.getUuid().equals(player.getUniqueId()))
                return rp;
        }
        return null;
    }

    public ReplicatedPlayer getReplicatedPlayer(UUID uuid) {
        for (ReplicatedPlayer rp : replicablePlayers) {
            if (rp.getUuid().equals(uuid))
                return rp;
        }
        return null;
    }

}