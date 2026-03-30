package org.jhotdraw.draw.figure;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.Color;
import java.awt.Font;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.utils.util.ResourceBundleUtil;

/**
 * Abstract base class shared by {@link TextFigure} and {@link TextAreaFigure}.
 *
 * Centralises the thirteen members that were previously duplicated across
 * both concrete classes.
 *
 * Methods whose behaviour differs between the two subclasses
 * ({@code getBaseline}, {@code getInsets}, {@code getTextColumns},
 * {@code isTextOverflow}) are intentionally left abstract so that each
 * subclass retains its own specialised implementation.
 */
public abstract class AbstractTextHolderFigure extends AbstractAttributedDecoratedFigure
    implements TextHolderFigure {

  protected boolean editable = true;

  protected AbstractTextHolderFigure() {
    this(ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels")
        .getString("TextFigure.defaultText"));
  }

  protected AbstractTextHolderFigure(String text) {
    setText(text);
  }

  /** Gets the text shown by the text figure. */
  @Override
  public String getText() {
    return attr().get(TEXT);
  }

  /** Sets the text shown by the text figure. */
  @Override
  public void setText(String newText) {
    attr().set(TEXT, newText);
  }

  @Override
  public Font getFont() {
    return AttributeKeys.getFont(this);
  }

  @Override
  public Color getTextColor() {
    return attr().get(TEXT_COLOR);
  }

  @Override
  public Color getFillColor() {
    return attr().get(FILL_COLOR);
  }

  @Override
  public void setFontSize(double size) {
    attr().set(FONT_SIZE, size);
  }

  @Override
  public double getFontSize() {
    return attr().get(FONT_SIZE);
  }

  @Override
  public int getTabSize() {
    return 8;
  }

  @Override
  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean b) {
    this.editable = b;
  }

  @Override
  public TextHolderFigure getLabelFor() {
    return this;
  }
}
