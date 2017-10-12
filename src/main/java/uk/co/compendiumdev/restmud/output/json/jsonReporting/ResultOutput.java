package uk.co.compendiumdev.restmud.output.json.jsonReporting;

import uk.co.compendiumdev.restmud.engine.game.playerevents.BroadcastGameMessage;

import java.util.List;

/**
 * Created by Alan on 21/12/2015.
 */
public class ResultOutput {

    public LastAction resultoutput;
    public InventoryReport inventory;
    public LookResultOutput look;
    public GetUserDetails userDetails;
    public GameMessages gameMessages;
    public List<GetUserDetails> users;
    public ObjectInspection inspectReport;


    public ResultOutput(LastAction lastAction) {
        this.resultoutput = lastAction;

        // transfer optional items from last action to result output
        inspectReport = lastAction.getObjectInspectionReport();
        lastAction.removeObjectInspectionReport();
        inventory = lastAction.getInventoryReport();
        lastAction.removeInventoryReport();
        users = lastAction.getUserDetailsList();
        lastAction.removeUserDetailsList();
    }

    public ResultOutput() {
    }

    public static ResultOutput getLastActionSuccess(String lastActionMessage) {

        return new ResultOutput(LastAction.createSuccess(lastActionMessage));
    }

    public static ResultOutput getLastActionError(String lastActionMessage) {
        return new ResultOutput(LastAction.createError(lastActionMessage));
    }


    public ResultOutput setInventoryContents(InventoryReport inventoryReport) {
        this.inventory = inventoryReport;
        return this;
    }


    public ResultOutput setLook(LookResultOutput look) {
        this.look = look;
        return this;
    }

    public ResultOutput setGameMessages(GameMessages messages) {
        this.gameMessages = messages;
        return this;
    }

    public ResultOutput setUserDetails(GetUserDetails userDetails) {
        this.userDetails = userDetails;
        return this;
    }

    public ResultOutput setUsersDetails(List<GetUserDetails> usersDetails) {
        this.users = usersDetails;
        return this;
    }

    public void setInspectReport(ObjectInspection inspectReport) {
        this.inspectReport = inspectReport;
    }

    public ResultOutput addGameMessages(List<BroadcastGameMessage> extraMessages) {

        if (extraMessages == null) {
            return this;
        }

        if (extraMessages.size() == 0) {
            return this;
        }

        if (gameMessages == null) {
            gameMessages = new GameMessages(extraMessages);
            setGameMessages(gameMessages);
        } else {
            gameMessages.addAdditionalMessages(extraMessages);
        }

        return this;
    }

    public ResultOutput addGameMessages(GameMessages extraMessages) {

        if (extraMessages == null) {
            return this;
        }

        if (extraMessages.messages.size() == 0) {
            return this;
        }

        if (gameMessages == null) {
            gameMessages = new GameMessages(extraMessages.messages);
            setGameMessages(gameMessages);
        } else {
            gameMessages.addAdditionalMessages(extraMessages.messages);
        }

        return this;
    }

    public static ResultOutput sorryIDontKnowHowToDoThat(String splatter) {
        LastAction lastAction;
        ResultOutput resultOutput;
        lastAction = LastAction.createError("Sorry, I don't know how to do that : " + splatter);
        resultOutput = new ResultOutput(lastAction);
        return resultOutput;
    }
}
