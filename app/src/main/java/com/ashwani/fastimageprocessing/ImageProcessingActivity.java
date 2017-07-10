package com.ashwani.fastimageprocessing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.android.imageprocessing.FastImageProcessingPipeline;
import project.android.imageprocessing.FastImageProcessingView;
import project.android.imageprocessing.filter.BasicFilter;
import project.android.imageprocessing.filter.colour.AdaptiveThresholdFilter;
import project.android.imageprocessing.filter.colour.AmatorkaFilter;
import project.android.imageprocessing.filter.colour.BrightnessFilter;
import project.android.imageprocessing.filter.colour.ChromaKeyFilter;
import project.android.imageprocessing.filter.colour.ColourInvertFilter;
import project.android.imageprocessing.filter.colour.ColourMatrixFilter;
import project.android.imageprocessing.filter.colour.ContrastFilter;
import project.android.imageprocessing.filter.colour.ExposureFilter;
import project.android.imageprocessing.filter.colour.FalseColourFilter;
import project.android.imageprocessing.filter.colour.GammaFilter;
import project.android.imageprocessing.filter.colour.GreyScaleFilter;
import project.android.imageprocessing.filter.colour.HazeFilter;
import project.android.imageprocessing.filter.colour.HighlightShadowFilter;
import project.android.imageprocessing.filter.colour.HueFilter;
import project.android.imageprocessing.filter.colour.LevelsFilter;
import project.android.imageprocessing.filter.colour.LookupFilter;
import project.android.imageprocessing.filter.colour.LuminanceThresholdFilter;
import project.android.imageprocessing.filter.colour.MissEtikateFilter;
import project.android.imageprocessing.filter.colour.MonochromeFilter;
import project.android.imageprocessing.filter.colour.OpacityFilter;
import project.android.imageprocessing.filter.colour.RGBFilter;
import project.android.imageprocessing.filter.colour.SaturationFilter;
import project.android.imageprocessing.filter.colour.SepiaFilter;
import project.android.imageprocessing.filter.colour.SoftEleganceFilter;
import project.android.imageprocessing.filter.colour.ToneCurveFilter;
import project.android.imageprocessing.filter.effect.BulgeDistortionFilter;
import project.android.imageprocessing.filter.effect.CGAColourSpaceFilter;
import project.android.imageprocessing.filter.effect.CrosshatchFilter;
import project.android.imageprocessing.filter.effect.EmbossFilter;
import project.android.imageprocessing.filter.effect.GlassSphereFilter;
import project.android.imageprocessing.filter.effect.HalftoneFilter;
import project.android.imageprocessing.filter.effect.KuwaharaRadius3Filter;
import project.android.imageprocessing.filter.effect.MosaicFilter;
import project.android.imageprocessing.filter.effect.PinchDistortionFilter;
import project.android.imageprocessing.filter.effect.PixellateFilter;
import project.android.imageprocessing.filter.effect.PolarPixellateFilter;
import project.android.imageprocessing.filter.effect.PolkaDotFilter;
import project.android.imageprocessing.filter.effect.PosterizeFilter;
import project.android.imageprocessing.filter.effect.SketchFilter;
import project.android.imageprocessing.filter.effect.SmoothToonFilter;
import project.android.imageprocessing.filter.effect.SphereRefractionFilter;
import project.android.imageprocessing.filter.effect.StretchDistortionFilter;
import project.android.imageprocessing.filter.effect.SwirlFilter;
import project.android.imageprocessing.filter.effect.ThresholdSketchFilter;
import project.android.imageprocessing.filter.effect.ToonFilter;
import project.android.imageprocessing.filter.effect.VignetteFilter;
import project.android.imageprocessing.filter.processing.BilateralBlurFilter;
import project.android.imageprocessing.filter.processing.BoxBlurFilter;
import project.android.imageprocessing.filter.processing.CannyEdgeDetectionFilter;
import project.android.imageprocessing.filter.processing.ClosingFilter;
import project.android.imageprocessing.filter.processing.ClosingRGBFilter;
import project.android.imageprocessing.filter.processing.ConvolutionFilter;
import project.android.imageprocessing.filter.processing.CropFilter;
import project.android.imageprocessing.filter.processing.DilationFilter;
import project.android.imageprocessing.filter.processing.DilationRGBFilter;
import project.android.imageprocessing.filter.processing.ErosionFilter;
import project.android.imageprocessing.filter.processing.ErosionRGBFilter;
import project.android.imageprocessing.filter.processing.FastBlurFilter;
import project.android.imageprocessing.filter.processing.FlipFilter;
import project.android.imageprocessing.filter.processing.GaussianBlurFilter;
import project.android.imageprocessing.filter.processing.GaussianBlurPositionFilter;
import project.android.imageprocessing.filter.processing.GaussianSelectiveBlurFilter;
import project.android.imageprocessing.filter.processing.MedianFilter;
import project.android.imageprocessing.filter.processing.MotionBlurFilter;
import project.android.imageprocessing.filter.processing.OpeningFilter;
import project.android.imageprocessing.filter.processing.OpeningRGBFilter;
import project.android.imageprocessing.filter.processing.SharpenFilter;
import project.android.imageprocessing.filter.processing.SingleComponentFastBlurFilter;
import project.android.imageprocessing.filter.processing.SingleComponentGaussianBlurFilter;
import project.android.imageprocessing.filter.processing.SobelEdgeDetectionFilter;
import project.android.imageprocessing.filter.processing.ThresholdEdgeDetectionFilter;
import project.android.imageprocessing.filter.processing.TiltShiftFilter;
import project.android.imageprocessing.filter.processing.TransformFilter;
import project.android.imageprocessing.filter.processing.UnsharpMaskFilter;
import project.android.imageprocessing.filter.processing.ZoomBlurFilter;
import project.android.imageprocessing.input.CameraPreviewInput;
import project.android.imageprocessing.input.GLTextureOutputRenderer;
import project.android.imageprocessing.input.ImageResourceInput;
import project.android.imageprocessing.output.ScreenEndpoint;

public class ImageProcessingActivity extends Activity {

    private FastImageProcessingView view;
    private List<BasicFilter> filters;
    private int curFilter;
    private GLTextureOutputRenderer input;
    private long touchTime;
    private FastImageProcessingPipeline pipeline;
    private ScreenEndpoint screen;
    private boolean usingCamera;

    private void addFilter(BasicFilter filter) {
        filters.add(filter);
    }

    private int finalHeight;
    private int finalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.activity_main);

        view = (FastImageProcessingView) findViewById(R.id.process);
        pipeline = new FastImageProcessingPipeline();
        view.setPipeline(pipeline);

        //	usingCamera = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
        usingCamera = false;


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        finalHeight = displayMetrics.heightPixels;
        finalWidth = displayMetrics.widthPixels;
       /* ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                container.getViewTreeObserver().removeOnPreDrawListener(this);

                return true;
            }
        });*/
        if (usingCamera) {
            input = new CameraPreviewInput(view);
        } else {
            resize(bitmap, finalWidth, finalHeight);
        }
        filters = new ArrayList<BasicFilter>();

        addFilter(new FlipFilter(FlipFilter.FLIP_HORIZONTAL));
        addFilter(new MosaicFilter(this, R.drawable.webcircles, new PointF(0.125f, 0.125f), new PointF(0.025f, 0.025f), 64, true));
        addFilter(new CGAColourSpaceFilter());
        addFilter(new KuwaharaRadius3Filter());
        //addFilter(new KuwaharaFilter(8)); //Will not work on devices that don't support for loop on shader
        addFilter(new VignetteFilter(new PointF(0.5f, 0.5f), new float[]{0.3f, 0.8f, 0.3f}, 0.3f, 0.75f));
        addFilter(new GlassSphereFilter(new PointF(0.43f, 0.5f), 0.25f, 0.71f, 0.5f));
        addFilter(new SphereRefractionFilter(new PointF(0.43f, 0.5f), 0.25f, 0.71f, 0.5f));
        addFilter(new StretchDistortionFilter(new PointF(0.5f, 0.5f)));
        addFilter(new PinchDistortionFilter(new PointF(0.43f, 0.5f), 0.25f, 0.5f, 0.5f));
        addFilter(new BulgeDistortionFilter(new PointF(0.43f, 0.5f), 0.25f, 0.5f, 0.5f));
        addFilter(new SwirlFilter(new PointF(0.4f, 0.5f), 0.5f, 1f));
        addFilter(new PosterizeFilter(2f));
        addFilter(new EmbossFilter(1.5f));
        addFilter(new SmoothToonFilter(0.25f, 0.5f, 5f));
        addFilter(new ToonFilter(0.4f, 10f));
        addFilter(new ThresholdSketchFilter(0.7f));
        addFilter(new SketchFilter());
        addFilter(new CrosshatchFilter(0.005f, 0.0025f));
        addFilter(new HalftoneFilter(0.01f, 1f));
        addFilter(new PolkaDotFilter(0.9f, 0.03f, 1f));
        addFilter(new PolarPixellateFilter(new PointF(0.4f, 0.5f), new PointF(0.05f, 0.05f)));
        addFilter(new PixellateFilter(0.01f, 1f));
        addFilter(new ZoomBlurFilter(2f, new PointF(0.4f, 0.5f)));
        addFilter(new MotionBlurFilter(2f, 45f));
        addFilter(new OpeningFilter(1));
        addFilter(new OpeningRGBFilter(3));
        addFilter(new ClosingFilter(2));
        addFilter(new ClosingRGBFilter(4));
        addFilter(new ErosionRGBFilter(3));
        addFilter(new ErosionFilter(1));
        addFilter(new DilationRGBFilter(2));
        addFilter(new DilationFilter(4));
        addFilter(new CannyEdgeDetectionFilter(1.0f, 0.1f, 0.4f));
        addFilter(new ThresholdEdgeDetectionFilter(0.6f));
        addFilter(new SobelEdgeDetectionFilter());
        addFilter(new TiltShiftFilter(4f, 0.4f, 0.6f, 0.2f));
        addFilter(new BilateralBlurFilter(1f));
        addFilter(new MedianFilter());
        addFilter(new GaussianBlurPositionFilter(4f, 1.2f, new PointF(0.4f, 0.5f), 0.5f, 0.1f));
        addFilter(new GaussianSelectiveBlurFilter(4f, 1.2f, new PointF(0.4f, 0.5f), 0.5f, 0.1f));
        addFilter(new SingleComponentGaussianBlurFilter(2.3f));
        addFilter(new SingleComponentFastBlurFilter());
        addFilter(new FastBlurFilter());
        addFilter(new UnsharpMaskFilter(2.0f, 0.5f));
        addFilter(new SharpenFilter(1f));
        //addFilter(new LanczosResamplingFilter(256, 128));
        addFilter(new CropFilter(0.25f, 0f, 0.75f, 1f));
        BasicFilter cFilter1 = new CropFilter(0.25f, 0f, 0.75f, 1f);
        cFilter1.rotateClockwise90Degrees(1);
        addFilter(cFilter1);
        BasicFilter cFilter2 = new CropFilter(0.25f, 0f, 0.75f, 1f);
        cFilter2.rotateClockwise90Degrees(2);
        addFilter(cFilter2);
        BasicFilter cFilter3 = new CropFilter(0.25f, 0f, 0.75f, 1f);
        cFilter3.rotateClockwise90Degrees(3);
        addFilter(cFilter3);
        addFilter(new TransformFilter(new float[]{
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                -0.5f, 0f, 0f, 1f
        }, false, false));
        addFilter(new ChromaKeyFilter(new float[]{1.0f, 0.3f, 0.0f}, 0.4f, 0.1f));
        addFilter(new AdaptiveThresholdFilter());
        addFilter(new BoxBlurFilter());
        addFilter(new LuminanceThresholdFilter(0.4f));
        addFilter(new OpacityFilter(0.5f));
        addFilter(new SepiaFilter());
        addFilter(new HazeFilter(0.3f, 0.1f));
        addFilter(new FalseColourFilter(new float[]{0.0f, 0.0f, 0.5f}, new float[]{1.0f, 0.0f, 0.0f}));
        addFilter(new MonochromeFilter(new float[]{1.0f, 0.8f, 0.8f}, 1.0f));
        addFilter(new ColourInvertFilter());
        addFilter(new SoftEleganceFilter(this));
        addFilter(new GaussianBlurFilter(2.3f));
        addFilter(new MissEtikateFilter(this));
        addFilter(new AmatorkaFilter(this));
        addFilter(new LookupFilter(this, R.drawable.lookup_soft_elegance_1));
        addFilter(new HighlightShadowFilter(0f, 1f));
        Point[] defaultCurve = new Point[]{new Point(128, 128), new Point(64, 0), new Point(192, 255)};
        addFilter(new ToneCurveFilter(defaultCurve, defaultCurve, defaultCurve, defaultCurve));
        addFilter(new HueFilter(3.14f / 6f));
        addFilter(new BrightnessFilter(0.5f));
        addFilter(new ColourMatrixFilter(new float[]{0.33f, 0f, 0f, 0f,
                0f, 0.67f, 0f, 0f,
                0f, 0f, 1.34f, 0f,
                0.2f, 0.2f, 0.2f, 1.0f}, 0.5f));
        addFilter(new RGBFilter(0.33f, 0.67f, 1.34f));
        addFilter(new GreyScaleFilter());
        addFilter(new ConvolutionFilter(new float[]{
                1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f,
                1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f,
                1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f,
                1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f,
                1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f, 1 / 25f}, 5, 5));
        addFilter(new ExposureFilter(0.95f));
        addFilter(new ContrastFilter(1.5f));
        addFilter(new SaturationFilter(0.5f));
        addFilter(new GammaFilter(1.75f));
        addFilter(new LevelsFilter(0.2f, 0.8f, 1f, 0f, 1f));

        screen = new ScreenEndpoint(pipeline);

        input.addTarget(screen);
        for (BasicFilter filter : filters) {
            filter.addTarget(screen);
        }

        pipeline.addRootRenderer(input);
        pipeline.startRendering();
        final Context context = this;
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                if (System.currentTimeMillis() - touchTime > 100) {
                    pipeline.pauseRendering();
                    touchTime = System.currentTimeMillis();
                    if (curFilter == 0) {
                        input.removeTarget(screen);
                    } else {
                        input.removeTarget(filters.get(curFilter - 1));
                        pipeline.addFilterToDestroy(filters.get(curFilter - 1));
                    }
                    curFilter = (curFilter + 1) % (filters.size() + 1);
                    if (curFilter == 0) {
                        input.addTarget(screen);
                    } else {
                        input.addTarget(filters.get(curFilter - 1));
                    }
                    pipeline.startRendering();
                    view.requestRender();
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (usingCamera) {
            ((CameraPreviewInput) input).onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usingCamera) {
            ((CameraPreviewInput) input).onResume();
        }
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalW = maxWidth;
            int finalH = maxHeight;
            if (ratioMax > 1) {
                finalW = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalH = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalW, finalH, true);


            if (finalW < finalWidth)
                finalWidth = finalW;
            if (finalH < finalHeight)
                finalHeight = finalH;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = finalWidth;
            layoutParams.height = finalHeight;
            view.setLayoutParams(layoutParams);
            input = new ImageResourceInput(view, image);
            return image;
        } else {
            return image;
        }
    }

}
