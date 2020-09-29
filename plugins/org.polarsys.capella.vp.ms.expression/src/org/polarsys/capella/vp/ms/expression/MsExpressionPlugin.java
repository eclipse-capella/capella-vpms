package org.polarsys.capella.vp.ms.expression;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class MsExpressionPlugin extends Plugin {

  /** The shared instance */
  private static MsExpressionPlugin __plugin;

  /**
   * Return the shared instance of the activator
   */
  public static MsExpressionPlugin getDefault() {
    return __plugin;
  }

  /**
   * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    __plugin = this;
  }

  /**
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    __plugin = null;
    super.stop(context);
  }

}
