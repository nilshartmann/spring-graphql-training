import * as React from "react";
import { Link as RouterLink } from "react-router-dom";

type LinkProps = {
  to: string;
  children: React.ReactNode;
};

export default function Link({ to, children }: LinkProps) {
  return (
    <RouterLink
      className="text-publy-blue-600 hover:text-publy-blue-800 hover:underline hover:decoration-publy-green-600 hover:decoration-2"
      to={to}
    >
      {children}
    </RouterLink>
  );
}
