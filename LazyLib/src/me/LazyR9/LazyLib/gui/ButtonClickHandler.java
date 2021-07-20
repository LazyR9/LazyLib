package me.LazyR9.LazyLib.gui;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;

/**
 * Annotation for marking a method to be called when a button is clicked.
 * <p>
 * A filter can also be applied, and the method will only get called if the event matches the filter.
 * These filters are:
 * <ul>
 * <li>
 * {@link Material} type
 * </li>
 * </ul>
 * </p>
 *
 * @author LazyR9
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ButtonClickHandler {
	
	Material type() default Material.AIR;

}
