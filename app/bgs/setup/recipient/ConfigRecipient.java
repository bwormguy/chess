package bgs.setup.recipient;

import bgs.setup.recipient.src.AbstractConfigRecipient;
import bgs.visual.src.IVisual;
import org.jetbrains.annotations.NotNull;

/**
 * @author Roman
 */
public class ConfigRecipient extends AbstractConfigRecipient {

    public ConfigRecipient(IVisual visual) {
        super(visual);
    }

    @Override
    public String[] findOutPlayersConfig(@NotNull String[] configItems) {
        String[] configData = new String[configItems.length];
        for (int index = 0; index < configItems.length; index++) {
            configData[index] = this.visual.showMessage("Please, input field: " + configItems[index], true).trim();
        }
        return configData;
    }
}
