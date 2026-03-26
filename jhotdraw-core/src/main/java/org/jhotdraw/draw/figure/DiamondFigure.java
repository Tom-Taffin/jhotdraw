/*
 * @(#)DiamondFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.*;
import java.awt.geom.*;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.connector.ChopDiamondConnector;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.utils.geom.Geom;

/**
 * A {@link Figure} with a diamond shape.
 *
 * <p>The diamond vertices are located at the midpoints of its enclosing rectangle.
 */
public class DiamondFigure extends AbstractAttributedFigure {

  private static final long serialVersionUID = 1L;

  /**
   * If the attribute IS_QUADRATIC is put to true, all sides of the diamond have the same length.
   */
  public static final AttributeKey<Boolean> IS_QUADRATIC =
      new AttributeKey<>("isQuadratic", Boolean.class, false);

  /** The bounds of the diamond figure. */
  private Rectangle2D.Double rectangle;

  public DiamondFigure() {
    this(0, 0, 0, 0);
  }

  public DiamondFigure(double x, double y, double width, double height) {
    rectangle = new Rectangle2D.Double(x, y, width, height);
    /*
    setFillColor(Color.white);
    setStrokeColor(Color.black);
    */
  }

  // DRAWING
  @Override
  protected void drawFill(Graphics2D g) {
    Rectangle2D.Double bounds = getNormalizedBounds();
    double grow =
        AttributeKeys.getPerpendicularFillGrowth(this, AttributeKeys.getScaleFactorFromGraphics(g));
    growBounds(bounds, grow);
    g.fill(createDiamondPath(bounds));
  }

  @Override
  protected void drawStroke(Graphics2D g) {
    Rectangle2D.Double bounds = getNormalizedBounds();
    double grow =
        AttributeKeys.getPerpendicularDrawGrowth(this, AttributeKeys.getScaleFactorFromGraphics(g));
    growBounds(bounds, grow);
    g.draw(createDiamondPath(bounds));
  }

  // SHAPE AND BOUNDS

  @Override
  public Rectangle2D.Double getBounds(double scale) {
    Rectangle2D.Double bounds = (Rectangle2D.Double) rectangle.clone();
    return bounds;
  }

  private Rectangle2D.Double getNormalizedBounds() {
    Rectangle2D.Double bounds = (Rectangle2D.Double) rectangle.clone();
    if (attr().get(IS_QUADRATIC)) {
      double side = Math.max(bounds.width, bounds.height);
      bounds.x -= (side - bounds.width) / 2;
      bounds.y -= (side - bounds.height) / 2;
      bounds.width = bounds.height = side;
    }
    return bounds;
  }

  private void growBounds(Rectangle2D.Double bounds, double grow) {
    if (grow != 0d) {
      double w = bounds.width / 2d;
      double h = bounds.height / 2d;
      double lineLength = Math.sqrt(w * w + h * h);
      double scale = grow / lineLength;
      double yb = scale * w;
      double xa = scale * h;
      double growx = ((yb * yb) / xa + xa);
      double growy = ((xa * xa) / yb + yb);
      Geom.grow(bounds, growx, growy);
    }
  }

  private Path2D.Double createDiamondPath(Rectangle2D.Double bounds) {
    Path2D.Double diamond = new Path2D.Double();
    diamond.moveTo((bounds.x + bounds.width / 2), bounds.y);
    diamond.lineTo((bounds.x + bounds.width), (bounds.y + bounds.height / 2));
    diamond.lineTo((bounds.x + bounds.width / 2), (bounds.y + bounds.height));
    diamond.lineTo(bounds.x, (bounds.y + bounds.height / 2));
    diamond.closePath();
    return diamond;
  }

  @Override
  public Rectangle2D.Double getDrawingArea(double scaleD) {
    Rectangle2D.Double bounds = getNormalizedBounds();
    double grow = AttributeKeys.getPerpendicularHitGrowth(this, scaleD);
    growBounds(bounds, grow);
    return bounds;
  }

  /** Checks if a Point2D.Double is inside the figure. */
  @Override
  public boolean contains(Point2D.Double p, double scaleDenominator) {
    Rectangle2D.Double bounds = getNormalizedBounds();
    double grow = AttributeKeys.getPerpendicularFillGrowth(this, scaleDenominator);
    growBounds(bounds, grow);
    return createDiamondPath(bounds).contains(p);
  }

  @Override
  public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
    rectangle.x = Math.min(anchor.x, lead.x);
    rectangle.y = Math.min(anchor.y, lead.y);
    rectangle.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
    rectangle.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
  }

  /**
   * Moves the Figure to a new location.
   *
   * @param tx the transformation matrix.
   */
  @Override
  public void transform(AffineTransform tx) {
    Point2D.Double anchor = getStartPoint();
    Point2D.Double lead = getEndPoint();
    setBounds(
        (Point2D.Double) tx.transform(anchor, anchor), (Point2D.Double) tx.transform(lead, lead));
  }

  @Override
  public void restoreTransformTo(Object geometry) {
    Rectangle2D.Double r = (Rectangle2D.Double) geometry;
    rectangle.x = r.x;
    rectangle.y = r.y;
    rectangle.width = r.width;
    rectangle.height = r.height;
  }

  @Override
  public Object getTransformRestoreData() {
    return rectangle.clone();
  }

  // ATTRIBUTES
  // EDITING
  // CONNECTING

  /**
   * Returns the Figures connector for the specified location. By default a ChopDiamondConnector is
   * returned.
   *
   * @see ChopDiamondConnector
   */
  @Override
  public Connector findConnector(Point2D.Double p, ConnectionFigure prototype) {
    return new ChopDiamondConnector(this);
  }

  @Override
  public Connector findCompatibleConnector(Connector c, boolean isStart) {
    return new ChopDiamondConnector(this);
  }

  // COMPOSITE FIGURES
  // CLONING

  @Override
  public DiamondFigure clone() {
    DiamondFigure that = (DiamondFigure) super.clone();
    that.rectangle = (Rectangle2D.Double) this.rectangle.clone();
    return that;
  }
  // EVENT HANDLING
}
