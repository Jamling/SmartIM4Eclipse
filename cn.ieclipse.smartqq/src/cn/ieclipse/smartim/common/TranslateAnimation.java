/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim.common;

import org.eclipse.swt.widgets.Shell;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月24日
 *       
 */
public class TranslateAnimation {
    Shell target;
    long duration;
    
    int startX;
    int endX;
    int startY;
    int endY;
    float stepX;
    float stepY;
    
    long interval = 50;
    
    public TranslateAnimation(int startX, int endX, int startY, int endY) {
        super();
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }
    
    public TranslateAnimation setTarget(Shell target) {
        this.target = target;
        return this;
    }
    
    public TranslateAnimation setDuration(long duration) {
        this.duration = duration;
        return this;
    }
    
    public void start() {
        if (duration <= 0 || target == null) {
            return;
        }
        stepX = ((endX - startX) * 1.0f / duration);
        stepY = ((endY - startY) * 1.0f / duration);
        
        new Thread() {
            public void run() {
                long start = interval;
                while (start <= duration) {
                    float x = startX + start * stepX;
                    float y = startY + start * stepY;
                    move(x, y);
                    start += interval;
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        //
                    }
                }
                move(endX, endY);
            };
        }.start();
    }
    
    private void move(final float x, final float y) {
        if (target != null && !target.isDisposed()) {
            target.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (target != null && !target.isDisposed()) {
                        target.setLocation((int) x, (int) y);
                    }
                }
            });
        }
    }
}
