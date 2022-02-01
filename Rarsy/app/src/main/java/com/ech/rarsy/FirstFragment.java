package com.ech.rarsy;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ech.rarsy.databinding.FragmentFirstBinding;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import rars.Globals;
import rars.Launch;

import rars.venus.VenusUIDelegate;

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
                try {
                    Globals.setGuiDelegate(new VenusUIDelegate() {
                        @Override
                        public String getInputString(String prompt, int maxlength, Boolean isPopup) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            final FutureObject<String> input = new FutureObject<>();
                            handler.post(() -> {
                                requestInput(view, input);
                            });
                            return input.get();
                        }
                    });
                    System.setOut(new OutStream(System.out));
                    final File outFile = File.createTempFile("prefix1", "suffix1.asm", dir);
                    Files.write(Paths.get(outFile.getAbsolutePath()), Arrays.asList(asmText.getText()), Charset.defaultCharset());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Launch.main(new String[]{"nc", outFile.getAbsolutePath()});
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void requestInput(View parent, FutureObject<String> result) {
        EditText inputEditTextField = new EditText(parent.getContext());
        AlertDialog dialog = new AlertDialog.Builder(parent.getContext())
                .setTitle("Enter value!")
                .setMessage("Your value:")
                .setView(inputEditTextField)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = inputEditTextField.getText().toString();
                        result.set(editTextInput);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
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

    public static class FutureObject<T> {
        T object;
        boolean ready;

        synchronized void set(T object) {
            this.object = object;
            this.ready = true;
            notifyAll();
        }

        T get() {
            while (!ready) {
                synchronized(this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
            }
            return object;
        }
    }
}