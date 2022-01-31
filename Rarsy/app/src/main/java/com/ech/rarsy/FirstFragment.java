package com.ech.rarsy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ech.rarsy.databinding.FragmentFirstBinding;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import rars.Launch;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView asmText;

    private static Context appContext = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        FirstFragment.appContext = getContext().getApplicationContext();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        asmText = (TextView)view.findViewById(R.id.asm_text);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dir = view.getContext().getCacheDir();
                File outFile = null;
                try {
                    System.setOut(new OutStream(System.out));
                    outFile = File.createTempFile("prefix1", "suffix1.asm", dir);
                    Files.write(Paths.get(outFile.getAbsolutePath()), Arrays.asList(asmText.getText()), Charset.defaultCharset());
                    Launch.main(new String[]{"nc", outFile.getAbsolutePath()});
                } catch (Throwable e) {
                    e.printStackTrace();
                }
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private static class OutStream extends PrintStream {
        public OutStream(OutputStream out) {
            super(out); //TODO: basic is OutputStream.write(...);
        }
        @Override
        public void println(String s) {
            super.println(s);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + s, Toast.LENGTH_LONG).show());
        }

        @Override
        public void print(String s) {
            super.print(s);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + s, Toast.LENGTH_LONG).show());
        }

        @Override
        public void print(int i) {
            super.print(i);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + i, Toast.LENGTH_LONG).show());
        }

        @Override
        public void print(long i) {
            super.print(i);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + i, Toast.LENGTH_LONG).show());
        }

        @Override
        public void println(int i) {
            super.println(i);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + i, Toast.LENGTH_LONG).show());
        }

        @Override
        public void println(long i) {
            super.println(i);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(FirstFragment.appContext, "MSG: " + i, Toast.LENGTH_LONG).show());
        }
    }
}