package com.velocitypackage.webj.materials.components.items;

import com.velocitypackage.webj.materials.hypertext.Bootstrap;
import com.velocitypackage.webj.materials.hypertext.HyperTextElement;
import com.velocitypackage.webj.materials.hypertext.Tag;

import java.util.Map;

public class Button extends Item
{
    public final String text;
    public final Runnable runnable;
    public final ButtonType buttonType;
    
    public Button(String text, Runnable r)
    {
        super.setHyperTextElement(new HyperTextElement(Tag.BUTTON, new Bootstrap[]{Bootstrap.BTN, Bootstrap.BTN_PRIMARY}, null, styles(), text));
        this.text = text;
        this.runnable = r;
        this.buttonType = ButtonType.PRIMARY;
    }
    
    public Button(String text, ButtonType buttonType, Runnable r)
    {
        if (buttonType == null) {
            buttonType = ButtonType.PRIMARY;
        }
        super.setHyperTextElement(new HyperTextElement(Tag.BUTTON, new Bootstrap[]{Bootstrap.BTN, buttonType.state}, null, styles(), text));
        this.text = text;
        this.runnable = r;
        this.buttonType = buttonType;
    }
    
    @Override
    public void onInteract(Map<String, String> values)
    {
        runnable.run();
    }
    
    public enum ButtonType
    {
        PRIMARY(Bootstrap.BTN_PRIMARY), SECONDARY(Bootstrap.BTN_SECONDARY), SUCCESS(Bootstrap.BTN_SUCCESS), DANGER(Bootstrap.BTN_DANGER), WARNING(Bootstrap.BTN_WARNING), INFO(Bootstrap.BTN_INFO), LIGHT(Bootstrap.BTN_LIGHT), DARK(Bootstrap.BTN_DARK), LINK(Bootstrap.BTN_LINK);
        public final Bootstrap state;
        ButtonType(Bootstrap state)
        {
            this.state = state;
        }
    }
}