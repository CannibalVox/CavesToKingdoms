package talonos.cavestokingdoms.proxies;

import mantle.client.MProxyClient;
import talonos.cavestokingdoms.client.pages.*;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() 
    {
    	super.registerRenderers();	
        
        MProxyClient.registerManualPage("c2kModularToolsPage", C2KModularToolsPage.class);
        MProxyClient.registerManualPage("extMaterialUsePage", ExtMaterialsUsagePage.class);
        MProxyClient.registerManualPage("bowMaterialsPage", BowMaterialsPage.class);
        MProxyClient.registerManualPage("c2kContentsPage", C2KContentsPage.class);
        MProxyClient.registerManualPage("c2kClassicToolsPage", C2KClassicToolsPage.class);
        MProxyClient.registerManualPage("c2kArmorPage", C2KArmorPage.class);
    }
}
