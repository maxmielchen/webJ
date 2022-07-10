package com.velocitypackage.webj.materials.webJ;

import java.util.Arrays;

public class NotSupportedMessageFormat extends Exception
{
    public NotSupportedMessageFormat()
    {
        super();
    }
    
    public NotSupportedMessageFormat(String... format)
    {
        super("Supported Formats: " + Arrays.toString(format));
    }
}