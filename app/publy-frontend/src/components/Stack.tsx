import React from "react";
import classNames from "classnames";

interface StackProps {
  children?: React.ReactNode;
  orientation?: "vertical" | "horizontal";
  space?: 5 | 10;
}

export default function Stack({
  children,
  orientation = "vertical",
  space = 5,
}: StackProps) {
  const className = classNames({
    "flex flex-row": orientation === "horizontal",
    "space-y-5": orientation === "vertical" && space === 5,
    "space-y-10": orientation === "vertical" && space === 10,
    "space-x-5": orientation === "horizontal" && space === 5,
    "space-x-10": orientation === "horizontal" && space === 10,
  });
  return <div className={className}>{children}</div>;
}
