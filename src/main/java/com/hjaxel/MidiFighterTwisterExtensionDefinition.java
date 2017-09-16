package com.hjaxel;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class MidiFighterTwisterExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("2cbd0a22-c0a6-44b9-a646-2da2d70939e2");

   public static final String[] DEVICE_DISCOVERY_NAME = {"Midi Fighter Twister"};

   public MidiFighterTwisterExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Midi Fighter Twister";
   }
   
   @Override
   public String getAuthor()
   {
      return "pawnbroker";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "DjTechTools";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Midi Fighter Twister";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 3;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 1;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 1;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
      if (platformType == PlatformType.WINDOWS)
      {
         list.add(DEVICE_DISCOVERY_NAME, DEVICE_DISCOVERY_NAME);
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         // list.add(new String[]{"Input Port 0"}, new String[]{"Output Port 0"});
      }
      else if (platformType == PlatformType.MAC)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         // list.add(new String[]{"Input Port 0"}, new String[]{"Output Port 0"});
      }
      else if (platformType == PlatformType.LINUX)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         // list.add(new String[]{"Input Port 0"}, new String[]{"Output Port 0"});
      }
   }

   @Override
   public MidiFighterTwisterExtension createInstance(final ControllerHost host)
   {
      return new MidiFighterTwisterExtension(this, host);
   }
}
