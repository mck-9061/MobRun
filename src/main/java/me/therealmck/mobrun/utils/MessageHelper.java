package me.therealmck.mobrun.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class MessageHelper {
    private final FileConfiguration config;

    private String cannotOpenRunGui;
    private String cannotOpenShopGui;
    private String lobbyFullMsg;
    private String runStart;
    private String passedLevel;
    private String runFinish;
    private String notEnoughPoints;
    private String boughtSuccessfully;
    private String displayRunPoints;
    private String didNotFinishInTime;
    private String backToGameAfterDisconnect;
    private String afterShutDown1;
    private String afterShutDown2;
    private String cannotLeave;
    private String createShopSuccess;
    private String createShopAlreadyExists;
    private String addItemToShopSuccess;
    private String addItemToShopInvalid;

    public MessageHelper(FileConfiguration config) {
        this.config = config;

        this.cannotOpenRunGui = config.getString("CanNotOpenRunGuiMsg");
        this.cannotOpenShopGui = config.getString("CanNotOpenShopGuiMsg");
        this.lobbyFullMsg = config.getString("LobbyFullMsg");
        this.runStart = config.getString("RunStartMsg");
        this.passedLevel = config.getString("PassedLevelMsg");
        this.runFinish = config.getString("RunFinishMsg");
        this.notEnoughPoints = config.getString("ShopNotEnoughPointsMsg");
        this.boughtSuccessfully = config.getString("ShopBoughtSuccessfullyMsg");
        this.displayRunPoints = config.getString("DisplayRunPointsMsg");
        this.didNotFinishInTime = config.getString("CouldNotFinishInTimeMsg");
        this.backToGameAfterDisconnect = config.getString("BackInGameMsg");
        this.afterShutDown1 = config.getString("AfterShutDown1Msg");
        this.afterShutDown2 = config.getString("AfterShutDown2Msg");
        this.cannotLeave = config.getString("CanNotLeaveMsg");
        this.createShopSuccess = config.getString("MobrunShopCreateCommandSuccessMsg");
        this.createShopAlreadyExists = config.getString("MobrunShopCreateCommandFailureMsg");
        this.addItemToShopSuccess = config.getString("MobrunShopAddCommandSuccessMsg");
        this.addItemToShopInvalid = config.getString("MobrunShopAddCommandFailureMsg");
    }

    public String getCannotOpenRunGui() {
        return cannotOpenRunGui;
    }

    public String getCannotOpenShopGui() {
        return cannotOpenShopGui;
    }

    public String getRunStart() {
        return runStart;
    }

    public String getPassedLevel() {
        return passedLevel;
    }

    public String getRunFinish() {
        return runFinish;
    }

    public String getNotEnoughPoints() {
        return notEnoughPoints;
    }

    public String getBoughtSuccessfully() {
        return boughtSuccessfully;
    }

    public String getDisplayRunPoints() {
        return displayRunPoints;
    }

    public String getDidNotFinishInTime() {
        return didNotFinishInTime;
    }

    public String getBackToGameAfterDisconnect() {
        return backToGameAfterDisconnect;
    }

    public String getAfterShutDown1() {
        return afterShutDown1;
    }

    public String getAfterShutDown2() {
        return afterShutDown2;
    }

    public String getCannotLeave() {
        return cannotLeave;
    }

    public String getCreateShopSuccess() {
        return createShopSuccess;
    }

    public String getCreateShopAlreadyExists() {
        return createShopAlreadyExists;
    }

    public String getAddItemToShopSuccess() {
        return addItemToShopSuccess;
    }

    public String getAddItemToShopInvalid() {
        return addItemToShopInvalid;
    }

    public String getLobbyFull() { return lobbyFullMsg; }
}
