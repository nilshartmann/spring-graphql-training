import * as React from "react";
import cn from "classnames";

type ButtonProps = {
  children: React.ReactNode;
  disabled?: boolean;
  onClick?(): void;
  type?: "primary" | "blue";
  small?: boolean;
};

export default function Button({
  children,
  disabled,
  onClick,
  type = "primary",
  small = false,
}: ButtonProps) {
  const className = cn({
    Button: true,
    "Button--primary": type === "primary",
    "Button--blue": type === "blue",
    "Button--small": small,
  });

  return (
    <button onClick={onClick} className={className} disabled={disabled}>
      {children}
    </button>
  );
}
