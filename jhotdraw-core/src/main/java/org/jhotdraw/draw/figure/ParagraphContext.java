package org.jhotdraw.draw.figure;

/**
 * Encapsulates the layout context for paragraph rendering in text figures.
 *
 * This class groups related parameters used for drawing and measuring paragraphs,
 * reducing method parameter count and improving code maintainability.
 */
public class ParagraphContext {
  private float verticalPos; // the top bound of the paragraph
  private float maxVerticalPos; // the bottom bound of the paragraph
  private float leftMargin; // the left bound of the paragraph
  private float rightMargin; // the right bound of the paragraph
  private float[] tabStops; // an array with tab stops
  private int tabCount; // the number of entries in tabStops which contain actual values

  /**
   * Creates a new paragraph context with default values.
   */
  public ParagraphContext() {
    this.verticalPos = 0.0f;
    this.maxVerticalPos = Float.MAX_VALUE;
    this.leftMargin = 0.0f;
    this.rightMargin = Float.MAX_VALUE;
    this.tabStops = new float[0];
    this.tabCount = 0;
  }

  public float getVerticalPos() {
    return verticalPos;
  }

  public void setVerticalPos(float verticalPos) {
    this.verticalPos = verticalPos;
  }

  public float getMaxVerticalPos() {
    return maxVerticalPos;
  }

  public void setMaxVerticalPos(float maxVerticalPos) {
    this.maxVerticalPos = maxVerticalPos;
  }

  public float getLeftMargin() {
    return leftMargin;
  }

  public void setLeftMargin(float leftMargin) {
    this.leftMargin = leftMargin;
  }

  public float getRightMargin() {
    return rightMargin;
  }

  public void setRightMargin(float rightMargin) {
    this.rightMargin = rightMargin;
  }

  public float[] getTabStops() {
    return tabStops;
  }

  public void setTabStops(float[] tabStops) {
    this.tabStops = tabStops;
  }

  public int getTabCount() {
    return tabCount;
  }

  public void setTabCount(int tabCount) {
    this.tabCount = tabCount;
  }
}
