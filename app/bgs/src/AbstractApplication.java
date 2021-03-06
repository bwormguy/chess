package bgs.src;

import bgs.area.IArea;
import bgs.brains.src.IPlayer;
import bgs.brains.vars.StepLog;
import bgs.setup.Ini.configList.ConfigFields;
import bgs.setup.alias.AreaAliasList;
import bgs.setup.alias.PlayerAliasList;
import bgs.setup.alias.exceptions.AliasNotFoundException;
import bgs.setup.alias.generators.AreaGenerator;
import bgs.setup.alias.generators.PlayerGenerator;
import bgs.tools.logger.Logger;
import bgs.visual.src.GameColors;
import bgs.visual.src.IVisual;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Roman
 */
public abstract class AbstractApplication {

    protected IVisual Visual;

    protected IArea Area;

    protected IPlayer[] Players;

    public AbstractApplication(IVisual Visual) {
        this.Visual = Visual;
    }

    protected IArea generateArea(final @NotNull String typeAlias) throws AliasNotFoundException, ReflectiveOperationException {
        Logger.getGlobalLogger().info("Generating the area..");
        return new AreaGenerator().generate(new AreaAliasList(), typeAlias);
    }

    protected IPlayer generatePlayer(final @NotNull String typeAlias, String Name, Color color) throws AliasNotFoundException, ReflectiveOperationException {
        Logger.getGlobalLogger().info("Player generating..");
        return (IPlayer) new PlayerGenerator().generate(new PlayerAliasList(), typeAlias).newInstance(this.Area, color, this.Visual, Name);
    }

    /**
     * Method returns correct color array which uses for players step order decide.
     */
    protected Color[] chooseGameColorsSequence(final @NotNull String param) {
        if (param.equals("") || param.equals("standard") || param.equals("first")) return new Color[]{
                GameColors.firstColor, GameColors.secondColor
        };
        return new Color[]{
                GameColors.secondColor, GameColors.firstColor
        };
    }

    public abstract void run(String configFilePath);

    /**
     * Main bgs config applier.
     */
    protected void applySettings(final @NotNull String[] configData) throws Exception {
        Logger.configureGlobalLogger(configData[ConfigFields.LOG_FILE_PATH]);
        try {
            this.Area = generateArea(configData[ConfigFields.AREA_TYPE]);
            this.Players = new IPlayer[]
                    {
                            this.generatePlayer(configData[ConfigFields.FIRST_PLAYER_TYPE], configData[ConfigFields.FIRST_PLAYER_NAME],
                                    this.chooseGameColorsSequence(configData[ConfigFields.STEP_ORDER])[0]),
                            this.generatePlayer(configData[ConfigFields.SECOND_PLAYER_TYPE], configData[ConfigFields.SECOND_PLAYER_NAME],
                                    this.chooseGameColorsSequence(configData[ConfigFields.STEP_ORDER])[1])
                    };
        } catch (AliasNotFoundException exception) {
            Logger.getGlobalLogger().critical(exception.getMessage());
            throw exception;
        }
        Logger.getGlobalLogger().info("Settings applied successfully.");
    }

    /**
     * Method starts main game circle.
     */
    protected void start() {
        IPlayer[] players = new IPlayer[this.Players.length];
        for (IPlayer player : this.Players) {
            if (player.getColor() == GameColors.firstColor) players[0] = player;
            if (player.getColor() == GameColors.secondColor) players[1] = player;
        }
        while (true) {
            boolean exit = false;
            for (IPlayer player : players
            ) {
                while (true) {
                    this.Visual.Draw(this.Area);
                    StepLog stepResult = player.step();
                    if (stepResult == StepLog.DEFEAT) {
                        exit = true;
                    }
                    if (stepResult == StepLog.STEP_IS_IMPOSSIBLE) continue;
                    break;
                }
                if (exit) break;
            }
            if (exit) break;
        }
    }
}
