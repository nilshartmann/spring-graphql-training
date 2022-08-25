import * as React from "react";
import classNames from "classnames";

type SectionProps = {
  invert?: boolean;
  narrow?: boolean;
  space_vertical?: 5 | 10;
  children: React.ReactNode;
};

export default function Section({
  children,
  invert,
  narrow,
  space_vertical,
}: SectionProps) {
  const className = classNames({
    "bg-publy-gray-50 p-4 mb-4": invert,
    "px-4 pb-8": !invert,
    "space-y-5": space_vertical === 5,
    "space-y-10": space_vertical === 10,
    "max-w-2xl mx-auto": narrow,
  });

  return <div className={`${className}`}>{children}</div>;
}
