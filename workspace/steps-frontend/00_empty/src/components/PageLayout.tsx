import * as React from "react";
import PageHeader from "./PageHeader";
import { NavBar } from "./Nav";
import classNames from "classnames";

type PageLayoutProps = {
  children?: React.ReactNode;
  title?: string;
  narrow?: boolean;
};

export default function PageLayout({ children, narrow }: PageLayoutProps) {
  const className = classNames(
    narrow ? "max-w-2xl" : "max-w-4xl",
    "mx-auto py-6"
  );
  return (
    <div>
      {/*<PageHeader>{title}</PageHeader>*/}
      <main className={className}>{children}</main>
    </div>
  );
}

export function AnonymousPageLayout({
  children,
  title,
  narrow,
}: PageLayoutProps) {
  const maxWith = narrow ? "max-w-2xl" : "max-w-7xl";
  const className = `${maxWith} mx-auto py-6 sm:px-6 lg:px-8`;
  return (
    <div>
      <NavBar />
      <PageHeader>{title}</PageHeader>
      <main className={className}>{children}</main>
    </div>
  );
}
