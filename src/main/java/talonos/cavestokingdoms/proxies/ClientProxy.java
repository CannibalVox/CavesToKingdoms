package talonos.cavestokingdoms.proxies;

import mantle.client.MProxyClient;
import talonos.cavestokingdoms.client.pages.BowMaterialsPage;
import talonos.cavestokingdoms.client.pages.C2KContentsPage;
import talonos.cavestokingdoms.client.pages.C2KModularToolsPage;
import talonos.cavestokingdoms.client.pages.ExtMaterialsUsagePage;

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
    }
}
