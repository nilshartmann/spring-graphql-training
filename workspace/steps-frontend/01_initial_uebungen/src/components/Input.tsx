import * as React from "react";

type InputProps = {
  value?: string;
  onChange?(e: React.ChangeEvent<HTMLInputElement>): void;

  action?: React.ReactNode;
  disabled?: boolean;
  error?: string | boolean | null;
  id?: string;
  label: string;
  name?: string;
  type?: "text" | "password" | "date";
};

const Input = React.forwardRef<HTMLInputElement, InputProps>(function Input(
  { action, disabled, error, id, label, name, onChange, type = "text", value },
  ref
) {
  return (
    <div className="py-3.5 w-full">
      <div className="col-span-3 sm:col-span-2">
        <label className="block">
          {label}
          <div className="mt-1 flex rounded-md shadow-sm">
            <input
              ref={ref}
              type={type}
              name={name}
              onChange={onChange}
              id={id}
              disabled={disabled}
              className="flex-1 bg-white p-2"
              value={value}
            />
            {action}
          </div>
        </label>
        {typeof error === "string" && (
          <p className="mt-2 text-orange-700">{error}</p>
        )}
      </div>
    </div>
  );
});

export default Input;
