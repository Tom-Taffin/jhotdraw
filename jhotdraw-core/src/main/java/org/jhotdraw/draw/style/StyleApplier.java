package org.jhotdraw.draw.style;

import static org.jhotdraw.draw.AttributeKeys.*;

import java.awt.Color;
import java.awt.Graphics2D;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.utils.geom.Dimension2DDouble;

/**
 * Applies visual styles (colors, strokes, opacity) to Graphics2D.
 */
public class StyleApplier {

  private final Figure figure;

  /**
   * Creates a style applier for a figure.
   *
   * @param figure The figure whose styles to apply
   */
  public StyleApplier(Figure figure) {
    this.figure = figure;
  }

  /**
   * Applies the fill style and executes the render callback.
   */
  public void applyFillStyle(Graphics2D g, Runnable renderCallback) {
    Color fillColor = figure.attr().get(FILL_COLOR);
    if (fillColor != null) {
      Float opacity = figure.attr().get(OPACITY);
      if (opacity != null && opacity < 1) {
        fillColor = new Color(fillColor.getRGB() & 0xffffff | ((int) (opacity * 256) << 24), true);
      }
      g.setColor(fillColor);
      renderCallback.run();
    }
  }

  /**
   * Applies the stroke style and executes the render callback.
   */
  public void applyStrokeStyle(Graphics2D g, double scale, Runnable renderCallback) {
    Color strokeColor = figure.attr().get(STROKE_COLOR);
    double strokeWidth = figure.attr().get(STROKE_WIDTH);
    if (strokeColor != null && strokeWidth >= 0d) {
      g.setStroke(AttributeKeys.getStroke(figure, scale));
      g.setColor(strokeColor);
      renderCallback.run();
    }
  }

  /**
   * Applies the text style (with shadow support) and executes the callback.
   */
  public void applyTextStyle(Graphics2D g, Runnable renderCallback) {
    Color textColor = figure.attr().get(TEXT_COLOR);
    if (textColor != null) {
      Color shadowColor = figure.attr().get(TEXT_SHADOW_COLOR);
      Dimension2DDouble shadowOffset = figure.attr().get(TEXT_SHADOW_OFFSET);

      if (shadowColor != null && shadowOffset != null) {
        g.translate(shadowOffset.width, shadowOffset.height);
        g.setColor(shadowColor);
        renderCallback.run();
        g.translate(-shadowOffset.width, -shadowOffset.height);
      }

      g.setColor(textColor);
      renderCallback.run();
    }
  }
}
