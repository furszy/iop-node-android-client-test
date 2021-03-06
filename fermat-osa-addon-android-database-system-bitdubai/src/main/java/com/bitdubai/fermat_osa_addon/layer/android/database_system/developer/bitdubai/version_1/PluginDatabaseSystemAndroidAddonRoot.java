package com.bitdubai.fermat_osa_addon.layer.android.database_system.developer.bitdubai.version_1;

import android.content.Context;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractAddon;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededOsContext;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_osa_addon.layer.android.database_system.developer.bitdubai.version_1.structure.AndroidPluginDatabaseSystem;

/**
 * This addon handles a layer of database representation.
 * Encapsulates all the necessary functions to manage the database , its tables and records.
 * For interfaces PluginDatabase the modules need to authenticate with their plugin ids
 * <p/>
 * Created by nattyco
 * Modified by lnacosta (laion.cj91@gmail.com) on 26/10/2015.
 */

public final class PluginDatabaseSystemAndroidAddonRoot extends AbstractAddon {

    @NeededOsContext
    private Context context;

    private FermatManager pluginDatabaseSystemManager;

    /**
     * Constructor without parameters.
     */
    public PluginDatabaseSystemAndroidAddonRoot() {
        super(new AddonVersionReference(new Version()));
    }

    @Override
    public final void start() throws CantStartPluginException {

        try {

            pluginDatabaseSystemManager = new AndroidPluginDatabaseSystem(context);

            super.start();

        } catch (final Exception e) {

            throw new CantStartPluginException(e, "Plugin Database System Manager starting.", "Unhandled Exception trying to start the Plugin Database System manager.");
        }
    }

    @Override
    public final FermatManager getManager() {
        return pluginDatabaseSystemManager;
    }

    /**
     * Set context for this addon root
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }
}
